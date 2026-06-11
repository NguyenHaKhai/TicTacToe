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
 * Stateless server using a one-request-per-turn protocol.
 * The client starts with "0|0", then the server creates and returns the
 * official initial board. Later requests send "move|board".
 */
public class ServerType3 {
    private static final int DEFAULT_PORT = 1234;
    private static final int HUMAN_ID = 1;
    private static final int COMPUTER_ID = 2;
    private static final String START_GAME_MESSAGE = "0|0";

    private final int port;

    public ServerType3() {
        this(DEFAULT_PORT);
    }

    public ServerType3(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Stateless Dumb server started on port " + port);

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

        ServerDumbMess response;
        try {
            response = process(requestLine);
        } catch (IllegalArgumentException e) {
            response = new ServerDumbMess(GameState.INVALID, "0");
        }
        output.println(response.toProtocolMessage());
        System.out.println(response.toProtocolMessage());
    }

    private ServerDumbMess process(String requestLine) {
        ClientDumbMess request = ClientDumbMess.parse(requestLine);
        System.out.println(request.toProtocolMessage());

        if (isStartGameRequest(request)) {
            Board2D newBoard = new Board2D();
            return createGame(newBoard);
        }

        Board2D board = new Board2D();
        try {
            board.updateBoard(request.boardMessage());
        } catch (IllegalArgumentException e) {
            return new ServerDumbMess(GameState.INVALID, request.boardMessage());
        }

        Player human = new HumanPlayer(
            HUMAN_ID,
            new ByteArrayInputStream((request.moveText() + "\n").getBytes(StandardCharsets.UTF_8))
        );
        Player computer = new ComputerPlayer(COMPUTER_ID);

        int humanMove = human.makeMove(board);
        GameState humanMoveState = mapHumanMoveState(humanMove);
        if (humanMoveState != GameState.CONT) {
            return responseFor(humanMoveState, board);
        }

        board.setCell(humanMove, human.getId());
        if (board.checkWinner3() == human.getId()) {
            return responseFor(GameState.WIN, board);
        }
        if (board.isFull()) {
            return responseFor(GameState.DRAW, board);
        }

        int computerMove = computer.makeMove(board);
        while (computerMove == -1) {
            computerMove = computer.makeMove(board); // attempt to find 1 valid move for computer
        }

        board.setCell(computerMove, computer.getId());
        if (board.checkWinner3() == computer.getId()) {
            return responseFor(GameState.LOST, board);
        }
        if (board.isFull()) {
            return responseFor(GameState.DRAW, board);
        }

        return responseFor(GameState.CONT, board);
    }

    private boolean isStartGameRequest(ClientDumbMess request) {
        return START_GAME_MESSAGE.equals(request.toProtocolMessage());
    }

    private ServerDumbMess createGame(Board2D board) {
        return responseFor(GameState.CONT, board);
    }

    private ServerDumbMess responseFor(GameState state, Board2D board) {
        String boardMessage = board.toMessage();
        return new ServerDumbMess(state, boardMessage);
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
