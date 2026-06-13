package vgu.hihi.ttt.basic.svclarchitecture.securestateless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import vgu.hihi.ttt.basic.Board2D;
import vgu.hihi.ttt.basic.GameState;
import vgu.hihi.ttt.basic.settings.Constant;

/**
 * Cheat Client who can make another board message(not the original one) and send to server
 */
public class CheatClient {
    private final String host;
    private final int port;
    private final String turnStart;
    private final Board2D board;
    private final Scanner scanner;
    private String hashBoard;

    public CheatClient() {
        this(Constant.DEFAULT_HOST, Constant.DEFAULT_PORT, Constant.DEFAULT_START);
    }

    public CheatClient(String host, int port, String turnStart) {
        this.host = host;
        this.port = port;
        this.turnStart = turnStart;
        this.board = new Board2D();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("========== TIC-TAC-TOE ==========");
        System.out.println("Connected mode: secure stateless TCP request/response");

        try {
            ServerSecureMess response = sendMessage(new ClientSecureMess("START", turnStart, "0"));
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
            System.out.println("Enter a board message to cheat(or to recover the original if you cheated). Enter if dont cheat:)");
            String boardMessage = scanner.nextLine().trim();


            try {
                ServerSecureMess response = sendOneTurn(moveText, boardMessage);
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

    private ServerSecureMess sendOneTurn(String moveText, String boardMess) throws IOException {
        if(boardMess.isEmpty()) return sendMessage(new ClientSecureMess(moveText, board.toMessage(), hashBoard));
        else return sendMessage(new ClientSecureMess(moveText, boardMess, hashBoard));
    }

    private ServerSecureMess sendMessage(ClientSecureMess request) throws IOException {
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

            return ServerSecureMess.parse(responseLine);
        }
    }

    private void updateLocalState(ServerSecureMess response) {
        if (!response.boardMessage().isBlank()) {
            board.updateBoard(response.boardMessage());
        }
        hashBoard = response.hashBoard();
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
        String host = Constant.DEFAULT_HOST;
        int port = Constant.DEFAULT_PORT;
        String turnStart = Constant.DEFAULT_START;

        if (args.length > 0) {
            turnStart = args[0];
        }
        if (args.length > 1) {
            host = args[1];
        }
        if (args.length > 2) {
            port = Integer.parseInt(args[2]);
        }

        new CheatClient(host, port, turnStart).start();
    }

}
