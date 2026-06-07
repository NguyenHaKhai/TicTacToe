package vgu.hihi.ttt.basic.svclarchitecture.stateless;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import vgu.hihi.ttt.basic.Board2D;
import vgu.hihi.ttt.basic.ComputerPlayer;
import vgu.hihi.ttt.basic.GameState;
import vgu.hihi.ttt.basic.HumanPlayer;
import vgu.hihi.ttt.basic.Player;

/**
 * Stateless Server:
 * 1. Accept client connection.
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
public class ServerType3 {
    private static final int DEFAULT_PORT = 1234;
    private static final int HUMAN_ID = 1;
    private static final int COMPUTER_ID = 2;

    private final int port;

    public ServerType3() {
        this(DEFAULT_PORT);
    }

    public ServerType3(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Stateless server started on port " + port);

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

        ServerDumbMess response = process(requestLine);
        output.println(response.toProtocolMessage());
        System.out.println(response.toProtocolMessage());
    }

    private ServerDumbMess process(String requestLine) {
        ClientDumbMess request = ClientDumbMess.parse(requestLine);
        System.out.println(request.toProtocolMessage());
        // if (request.moveText().equalsIgnoreCase("q")){
        //     return new ServerDumbMess(GameState.END, request.boardMessage());
        // }

        // try {
        //     Integer.parseInt(request.moveText());
        // } catch (NumberFormatException e) {
        //     return new ServerDumbMess(GameState.INVALID, request.boardMessage());
        // }

        Board2D board = new Board2D();
        board.updateBoard(request.boardMessage());

        Player human = new HumanPlayer(
            HUMAN_ID,
            new ByteArrayInputStream((request.moveText() + "\n").getBytes(StandardCharsets.UTF_8))
        );
        Player computer = new ComputerPlayer(COMPUTER_ID);

        int humanMove = human.makeMove(board);
        GameState humanMoveState = mapHumanMoveState(humanMove);
        if (humanMoveState != GameState.CONT) {
            return new ServerDumbMess(humanMoveState, board.toMessage());
        }

        board.setCell(humanMove, human.getId());
        if (board.checkWinner3() == human.getId()) {
            return new ServerDumbMess(GameState.WIN, board.toMessage());
        }
        if (board.isFull()) {
            return new ServerDumbMess(GameState.DRAW, board.toMessage());
        }

        int computerMove = computer.makeMove(board);
        while(computerMove == -1) computerMove = computer.makeMove(board); // attempt until find a valid move

        board.setCell(computerMove, computer.getId());
        if (board.checkWinner3() == computer.getId()) {
            return new ServerDumbMess(GameState.LOST, board.toMessage());
        }
        if (board.isFull()) {
            return new ServerDumbMess(GameState.DRAW, board.toMessage());
        }

        return new ServerDumbMess(GameState.CONT, board.toMessage());
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
            new ServerType3(port).start();
        } catch (IOException e) {
            System.err.println("Failed to start Type 3 server: " + e.getMessage());
        }
    }

}
