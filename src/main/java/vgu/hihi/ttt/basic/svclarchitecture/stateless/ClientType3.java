package vgu.hihi.ttt.basic.svclarchitecture.stateless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import vgu.hihi.ttt.basic.Board2D;
import vgu.hihi.ttt.basic.GameState;

/**
 * Stateless client using a one-request-per-turn protocol.
 * The server sends the official initial board after the start request.
 */
public class ClientType3 {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1234;

    private final String host;
    private final int port;
    private final Board2D board;
    private final Scanner scanner;

    public ClientType3() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public ClientType3(String host, int port) {
        this.host = host;
        this.port = port;
        this.board = new Board2D();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("========== TIC-TAC-TOE ==========");
        System.out.println("Connected mode: stateless TCP request/response");

        try {
            ServerDumbMess response = sendMessage(new ClientDumbMess("0", "0"));
            updateLocalState(response);
        } catch (IOException e) {
            System.err.println("Could not start game: " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid server response: " + e.getMessage());
            return;
        }

        boolean playing = true;
        while (playing) {
            System.out.println();
            board.printBoard();
            System.out.print("Enter a move [1-" + board.getSize() + "] or q to quit: ");

            if (!scanner.hasNextLine()) {
                return;
            }

            String moveText = scanner.nextLine().trim();
            if (moveText.isEmpty()) {
                System.out.println("Please enter a move.");
                continue;
            }

            try {
                ServerDumbMess response = sendOneTurn(moveText);
                System.out.println(response.toProtocolMessage());
                updateLocalState(response);
                printResult(response.state());
                playing = !isTerminal(response.state());
            } catch (IOException e) {
                System.err.println("Could not complete request: " + e.getMessage());
                return;
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid server response: " + e.getMessage());
                return;
            }
        }
        System.out.println();
        board.printBoard();
    }

    private ServerDumbMess sendOneTurn(String moveText) throws IOException {
        return sendMessage(new ClientDumbMess(moveText, board.toMessage()));
    }

    private ServerDumbMess sendMessage(ClientDumbMess request) throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
             );
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            output.println(request.toProtocolMessage());

            String responseLine = input.readLine();
            if (responseLine == null) {
                throw new IOException("server closed the connection without a response");
            }

            return ServerDumbMess.parse(responseLine);
        }
    }

    private void updateLocalState(ServerDumbMess response) {
        if (!response.boardMessage().isBlank()) {
            board.updateBoard(response.boardMessage());
        }
    }

    private void printResult(GameState state) {
        switch (state) {
            case INVALID -> System.out.println("Invalid move. Try again.");
            case OCCUPIED -> System.out.println("That cell is occupied. Try again.");
            case END -> System.out.println("Game ended.");
            case CONT -> System.out.println("Computer moved. Your turn.");
            case WIN -> System.out.println("You won.");
            case LOST -> System.out.println("Computer won.");
            case DRAW -> System.out.println("Draw.");
        }
    }

    private boolean isTerminal(GameState state) {
        return state == GameState.END
            || state == GameState.WIN
            || state == GameState.LOST
            || state == GameState.DRAW;
    }

    public static void main(String[] args) {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        if (args.length > 0) {
            host = args[0];
        }
        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        new ClientType3(host, port).start();
    }

}
