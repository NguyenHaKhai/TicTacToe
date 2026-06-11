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
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import vgu.hihi.ttt.basic.Board2D;
import vgu.hihi.ttt.basic.ComputerPlayer;
import vgu.hihi.ttt.basic.GameState;
import vgu.hihi.ttt.basic.HumanPlayer;
import vgu.hihi.ttt.basic.Player;
// TODO rewrite the javadoc to review the code
/**
 * Stateless Server:
 * 1. Accept client connection and first start message.
 * 2. Read one request line.
 * 3. Parse move and board state.
 * 4. Reconstruct Board2D from the board state.
 * 5. Validate:
 *  - move is number
 *  - move is in range
 *  - target cell is empty
 *  - board shape is valid
 * 6. Apply human move.
 * 7. Check winner/draw/quit.
 * 8. If game continues, apply computer move.
 * 9. Check winner/draw again.
 * 10. Send structured response with official updated board.
 */
public class ServerType5 {
    private static final int DEFAULT_PORT = 1234;
    private static final int HUMAN_ID = 1;
    private static final int COMPUTER_ID = 2;
    private static final String START_GAME_MESSAGE = "0|0|0|0";

    private final int port;
    private final String secretKey;
    private final Map<String, String> gameHashes;

    public ServerType5() {
        this(DEFAULT_PORT);
    }

    public ServerType5(int port) {
        this.port = port;
        this.secretKey = UUID.randomUUID().toString();
        this.gameHashes = new HashMap<>();
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Stateless Secure server started on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected!");
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Client request failed: " + e.getMessage());
                }
            }
        }
    }

    private void handleClient(Socket clientSocket) throws IOException {
        BufferedReader input = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)
        );
        PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);

        String requestLine = input.readLine();
        if (requestLine == null) {
            return;
        }

        ServerAdvancedMess response;
        try {
            response = process(requestLine);
        } catch (IllegalArgumentException e) {
            response = new ServerAdvancedMess(GameState.INVALID, "0", "0", "0");
        }
        output.println(response.toProtocolMessage());
        System.out.println(response.toProtocolMessage());
    }

    private ServerAdvancedMess process(String requestLine) {
        ClientAdvancedMess request = ClientAdvancedMess.parse(requestLine);
        System.out.println(request.toProtocolMessage());

        if (isStartGameRequest(request)) {
            Board2D newBoard = new Board2D();
            return createGame(newBoard);
        }

        String storedHash = gameHashes.get(request.gameId());
        if (storedHash == null) {
            return new ServerAdvancedMess(GameState.INVALID, request.boardMessage(), request.hashBoard(), request.gameId());
        }

        // protect integrity and defend against replay attacks
        String requestBoardHash = hashBoard(request.boardMessage());
        if (!storedHash.equals(request.hashBoard()) || !storedHash.equals(requestBoardHash)) {
            return new ServerAdvancedMess(GameState.INVALID, request.boardMessage(), storedHash, request.gameId());
        }

        Board2D board = new Board2D();
        try {
            board.updateBoard(request.boardMessage());
        } catch (IllegalArgumentException e) {
            return new ServerAdvancedMess(GameState.INVALID, request.boardMessage(), storedHash, request.gameId());
        }

        Player human = new HumanPlayer(
            HUMAN_ID,
            new ByteArrayInputStream((request.moveText() + "\n").getBytes(StandardCharsets.UTF_8))
        );
        Player computer = new ComputerPlayer(COMPUTER_ID);

        int humanMove = human.makeMove(board);
        GameState humanMoveState = mapHumanMoveState(humanMove);
        if (humanMoveState != GameState.CONT) {
            if (humanMoveState == GameState.END) {
                return finishGame(request.gameId(), humanMoveState, board);
            }
            return responseFor(request.gameId(), humanMoveState, board);
        }

        board.setCell(humanMove, human.getId());
        if (board.checkWinner3() == human.getId()) {
            return finishGame(request.gameId(), GameState.WIN, board);
        }
        if (board.isFull()) {
            return finishGame(request.gameId(), GameState.DRAW, board);
        }

        int computerMove = computer.makeMove(board);
        while(computerMove == -1) computerMove = computer.makeMove(board); // attempt until find a valid move

        board.setCell(computerMove, computer.getId());
        if (board.checkWinner3() == computer.getId()) {
            return finishGame(request.gameId(), GameState.LOST, board);
        }
        if (board.isFull()) {
            return finishGame(request.gameId(), GameState.DRAW, board);
        }

        return responseFor(request.gameId(), GameState.CONT, board);
    }

    private boolean isStartGameRequest(ClientAdvancedMess request) {
        return START_GAME_MESSAGE.equals(request.toProtocolMessage());
    }

    private ServerAdvancedMess createGame(Board2D board) {
        String gameId = UUID.randomUUID().toString();
        return responseFor(gameId, GameState.CONT, board);
    }

    private ServerAdvancedMess responseFor(String gameId, GameState state, Board2D board) {
        String boardMessage = board.toMessage();
        String hashBoard = hashBoard(boardMessage);
        gameHashes.put(gameId, hashBoard);
        return new ServerAdvancedMess(state, boardMessage, hashBoard, gameId);
    }

    private ServerAdvancedMess finishGame(String gameId, GameState state, Board2D board) {
        String boardMessage = board.toMessage();
        String hashBoard = hashBoard(boardMessage);
        gameHashes.remove(gameId);
        return new ServerAdvancedMess(state, boardMessage, hashBoard, gameId);
    }

    private String hashBoard(String boardMessage) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((boardMessage + secretKey).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }

    private GameState mapHumanMoveState(int move) {
        return switch (move) {
            case -3 -> GameState.END;
            case -2 -> GameState.OCCUPIED;
            case -1 -> GameState.INVALID;
            default -> GameState.CONT;
        };
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            new ServerType5(port).start();
        } catch (IOException e) {
            System.err.println("Failed to start Type 4 server: " + e.getMessage());
        }
    }

}
