package vgu.hihi.ttt.basic.svclarchitecture.securestateless;

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
import java.util.UUID;

import vgu.hihi.ttt.basic.Board;
import vgu.hihi.ttt.basic.Board2D;
import vgu.hihi.ttt.basic.ComputerPlayer;
import vgu.hihi.ttt.basic.GameLogic;
import vgu.hihi.ttt.basic.GameState;
import vgu.hihi.ttt.basic.HumanPlayer;
import vgu.hihi.ttt.basic.Player;
import vgu.hihi.ttt.basic.settings.Constant;
// TODO rewrite the javadoc to review the code
/**
 * Stateless secure server using a one-request-per-turn protocol.
 * The client starts with "START|[who moves first]|0", then the server creates and returns the
 * official initial board. Later requests send "move|board".
 * START|1|0 for human to start
 * START|2|0 for computer to start
 */
public class ServerType4 {
    private final int port;
    private final String secretKey;

    public ServerType4() {
        this(Constant.DEFAULT_PORT);
    }

    public ServerType4(int port) {
        this.port = port;
        this.secretKey = UUID.randomUUID().toString();
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

        ServerSecureMess response;
        try {
            response = process(requestLine);
        } catch (IllegalArgumentException e) {
            response = new ServerSecureMess(GameState.INVALID, "0", "0");
        }
        output.println(response.toProtocolMessage());
        System.out.println(response.toProtocolMessage());
    }

    private ServerSecureMess process(String requestLine) {
        ClientSecureMess request = ClientSecureMess.parse(requestLine);
        System.out.println(request.toProtocolMessage());

        if (isStartGameRequest(request)) {
            String turnStart = request.boardMessage();
            Board newBoard = new Board2D();
            return createGame(newBoard, turnStart);
        }

        // Protect board integrity. Replay protection can be added by the game-id based protocol.
        String requestBoardHash = hashBoard(request.boardMessage());
        if (!request.hashBoard().equals(requestBoardHash)) {
            return new ServerSecureMess(GameState.INVALID, request.boardMessage(), request.hashBoard());
        }

        Board board = new Board2D();
        try {
            board.updateBoard(request.boardMessage());
        } catch (IllegalArgumentException e) {
            return new ServerSecureMess(GameState.INVALID, request.boardMessage(), request.hashBoard());
        }

        Player human = new HumanPlayer(
            Constant.HUMAN_ID,
            new ByteArrayInputStream((request.moveText() + "\n").getBytes(StandardCharsets.UTF_8))
        );
        Player computer = new ComputerPlayer(Constant.COMPUTER_ID);

        int humanMove = human.makeMove(board);
        GameState humanMoveState = GameLogic.applyMove(board, human, humanMove);
        if (humanMoveState != GameState.CONT) {
            return responseFor(humanMoveState, board);
        }

        int computerMove = computer.makeMove(board);
        while(computerMove == -1) computerMove = computer.makeMove(board); // attempt until find a valid move

        GameState compMoveState = GameLogic.applyMove(board, computer, computerMove);
        switch(compMoveState){
            case GameState.WIN -> {return responseFor(GameState.LOST, board);} // send lost message to client
            case GameState.DRAW -> {return responseFor(GameState.DRAW, board);}
            case GameState.CONT -> {return responseFor(GameState.CONT, board);}
            default -> {System.out.println("Something is wrong. Game State of computer: " + compMoveState);
                return responseFor(GameState.INVALID, board);
            }
        }
    }

    private boolean isStartGameRequest(ClientSecureMess request) {
        return "START".equals(request.moveText());
    }

    private ServerSecureMess createGame(Board board, String turnStart) {
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

    private ServerSecureMess responseFor(GameState state, Board board) {
        String boardMessage = board.toMessage();
        String hashBoard = hashBoard(boardMessage);
        return new ServerSecureMess(state, boardMessage, hashBoard);
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

    public static void main(String[] args) {
        int port = Constant.DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            new ServerType4(port).start();
        } catch (IOException e) {
            System.err.println("Failed to start Type 4 server: " + e.getMessage());
        }
    }

}
