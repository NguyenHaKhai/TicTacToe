package vgu.hihi.ttt.basic.svclarchitecture.advancedsecure;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import vgu.hihi.ttt.basic.Board;
import vgu.hihi.ttt.basic.Board2D;
import vgu.hihi.ttt.basic.ComputerPlayer;
import vgu.hihi.ttt.basic.GameLogic;
import vgu.hihi.ttt.basic.GameState;
import vgu.hihi.ttt.basic.HumanPlayer;
import vgu.hihi.ttt.basic.Player;
import vgu.hihi.ttt.basic.settings.Constant;

/**
 * Stateless secure server using START/CHALLENGE and MOVE request packets.
 * Board state is carried in the protocol; the server stores only consumed nonces
 * long enough to reject replayed packets inside the token lifetime.
 */
public class ServerType5 {

    private final int port;
    private final SecretKeySpec secretKey;
    private final SecureRandom secureRandom;
    private final ScheduledExecutorService cleanupWorker;
    private final ConcurrentSkipListSet<ExpirationEntry> expirationEntries;
    private final Set<String> consumedNonces;

    public ServerType5() {
        this(Constant.DEFAULT_PORT);
    }

    public ServerType5(int port) {
        this.port = port;
        this.secretKey = new SecretKeySpec(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8), Constant.HMAC_ALGORITHM);
        this.secureRandom = new SecureRandom();
        this.cleanupWorker = Executors.newSingleThreadScheduledExecutor();
        this.expirationEntries = new ConcurrentSkipListSet<>();
        this.consumedNonces = ConcurrentHashMap.newKeySet();
    }

    public void start() throws IOException {
        startCleanupWorker();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Advanced secure stateless server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } finally {
            cleanupWorker.shutdown();
        }
    }

    private void startCleanupWorker() {
        cleanupWorker.scheduleAtFixedRate(
            this::evictExpiredNonces,
            Constant.CLEANUP_PERIOD.toSeconds(),
            Constant.CLEANUP_PERIOD.toSeconds(),
            TimeUnit.SECONDS
        );
    }

    private void handleClient(Socket clientSocket) {
        try (clientSocket;
             BufferedReader input = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)
             );
             PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            String requestLine = input.readLine();
            if (requestLine == null) {
                return;
            }

            ServerAdvancedMess response;
            try {
                response = process(requestLine);
            } catch (IllegalArgumentException e) {
                System.out.println("Failed to process server repsonse?");
                response = new ServerAdvancedMess(GameState.INVALID, "0", 0L, "0", "0");
            }

            output.println(response.toProtocolMessage());
            System.out.println(response.toProtocolMessage());
        } catch (IOException e) {
            System.err.println("Client request failed: " + e.getMessage());
        }
    }

    private ServerAdvancedMess process(String requestLine) {
        ClientAdvancedMess request = ClientAdvancedMess.parse(requestLine);
        System.out.println(request.toProtocolMessage());

        if (isStartGameRequest(request)) {
            String turnStart = request.nonce();
            Board newBoard = new Board2D();
            return createGame(newBoard, turnStart);
        }

        String expectedHash = hmac(request.boardMessage(), request.nonce(), request.creationTime());
        if (!MessageDigest.isEqual(expectedHash.getBytes(StandardCharsets.UTF_8), request.hash().getBytes(StandardCharsets.UTF_8))){
            System.out.println("Wrong hash?");
            return new ServerAdvancedMess(GameState.INVALID, request.nonce(), request.creationTime(), request.boardMessage(), request.hash());
        }

        long now = System.currentTimeMillis();
        long expirationTime = request.creationTime() + Constant.TOKEN_TTL.toMillis();
        if (now > expirationTime) {
            // Calculate exactly how many milliseconds past the deadline the request arrived
            long msOverdue = now - expirationTime;
            long totalTimeTakenMs = now - request.creationTime();
            
            // Log it to your server console
            System.out.printf("[TIMEOUT] Move rejected. Total time taken: %,d ms (Max allowed: %,d ms). Overdue by: %,d ms.%n", 
                            totalTimeTakenMs, Constant.TOKEN_TTL.toMillis(), msOverdue);
            return new ServerAdvancedMess(GameState.TIMEOUT, request.nonce(), request.creationTime(), request.boardMessage(), request.hash());
        }

        if (!consumedNonces.add(request.nonce())) {
            return new ServerAdvancedMess(GameState.REPLAY, request.nonce(), request.creationTime(), request.boardMessage(), request.hash());
        }
        expirationEntries.add(new ExpirationEntry(expirationTime, request.nonce()));

        Board board = new Board2D();
        try {
            board.updateBoard(request.boardMessage());
        } catch (IllegalArgumentException e) {
            return new ServerAdvancedMess(GameState.INVALID, request.nonce(), request.creationTime(), request.boardMessage(), request.hash());
        }

        return processTurn(request.moveText(), board);
    }

    private ServerAdvancedMess processTurn(String moveText, Board board) {
        Player human = new HumanPlayer(
            Constant.HUMAN_ID,
            new ByteArrayInputStream((moveText + "\n").getBytes(StandardCharsets.UTF_8))
        );
        Player computer = new ComputerPlayer(Constant.COMPUTER_ID);

        GameState humanState = GameLogic.applyMove(board, human, human.makeMove(board));
        if (humanState != GameState.CONT) {
            return responseFor(humanState, board);
        }

        int computerMove = computer.makeMove(board);
        while (computerMove == -1) {
            computerMove = computer.makeMove(board);
        }

        GameState computerState = GameLogic.applyMove(board, computer, computerMove);
        switch(computerState){
            case GameState.WIN -> {return responseFor(GameState.LOST, board);} // send lost message to client
            case GameState.DRAW -> {return responseFor(GameState.DRAW, board);}
            case GameState.CONT -> {return responseFor(GameState.CONT, board);}
            default -> {System.out.println("Something is wrong. Game State of computer: " + computerState);
                return responseFor(GameState.INVALID, board);
            }
        }
    }

    private ServerAdvancedMess responseFor(GameState state, Board board) {
        String boardMessage = board.toMessage();
        long creationTime = System.currentTimeMillis();
        String nonce = newNonce();
        String hash = hmac(boardMessage, nonce, creationTime);
        return new ServerAdvancedMess(state, nonce, creationTime, boardMessage, hash);
    }

    private String hmac(String boardMessage, String nonce, long creationTime) {
        try {
            Mac mac = Mac.getInstance(Constant.HMAC_ALGORITHM);
            mac.init(secretKey);
            byte[] bytes = mac.doFinal((boardMessage + nonce + creationTime).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new IllegalStateException("HMAC-SHA256 is not available", e);
        }
    }

    private boolean isStartGameRequest(ClientAdvancedMess request) {
        return "START".equals(request.moveText());
    }

    private ServerAdvancedMess createGame(Board board, String turnStart) {
        if(turnStart.equals(String.valueOf(Constant.COMPUTER_ID))) { // computer moves first
            Player computer = new ComputerPlayer(Constant.COMPUTER_ID);
            int computerMove = computer.makeMove(board);
            while (computerMove == -1) {
                computerMove = computer.makeMove(board); // attempt to find 1 valid move for computer
            }
            board.setCell(computerMove, computer.getId()); 
        }
        return responseFor(GameState.CONT, board);
    }

    private String newNonce() {
        byte[] bytes = new byte[Constant.NONCE_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private void evictExpiredNonces() {
        long now = System.currentTimeMillis();
        int evicted = 0;

        while (evicted < Constant.MAX_BATCH_SIZE) {
            ExpirationEntry oldest = expirationEntries.isEmpty() ? null : expirationEntries.first();
            if (oldest == null || oldest.expirationTime() >= now) {
                return;
            }
            if (expirationEntries.remove(oldest)) {
                consumedNonces.remove(oldest.nonce());
                evicted++;
            }
        }
    }

    public static void main(String[] args) {
        int port = Constant.DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            new ServerType5(port).start();
        } catch (IOException e) {
            System.err.println("Failed to start Type 5 server: " + e.getMessage());
        }
    }
}
