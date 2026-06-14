package vgu.hihi.ttt.basic.svclarchitecture.advancedsecure;

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
// TODO rewrite the javadoc to review the code
/**
 * Client that needs to send more things. It is hard to commit replay attacks now
 */
public class ReplayCheatClient {
    private final String host;
    private final int port;
    private final String turnStart;
    private final Board2D board;
    private final Scanner scanner;
    private String hash;
    private String nonce;
    private long creationTime;

    public ReplayCheatClient() {
        this(Constant.DEFAULT_HOST, Constant.DEFAULT_PORT, Constant.DEFAULT_START);
    }

    public ReplayCheatClient(String host, int port, String turnStart) {
        this.host = host;
        this.port = port;
        this.turnStart = turnStart;
        this.board = new Board2D();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("========== TIC-TAC-TOE ==========");
        System.out.println("Connected mode: advanced secure stateless TCP request/response");

        try {
            ServerAdvancedMess response = sendMessage(new ClientAdvancedMess("START", turnStart, 0, "0", "0"));
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
            System.out.print("Enter a ClientAdvancedMessage to cheat: ");

            if (!scanner.hasNextLine()) {
                return;
            }

            String designMess = scanner.nextLine().trim();
            if (designMess.isEmpty()) {
                System.out.println("Please enter a whole client message to cheat.");
                continue;
            }

            try {
                ServerAdvancedMess response = sendReplayMessage(designMess);
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

    private ServerAdvancedMess sendReplayMessage(String designMess) throws IOException {
        return sendMessage(ClientAdvancedMess.parse(designMess));
    }

    private ServerAdvancedMess sendMessage(ClientAdvancedMess request) throws IOException {
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

            return ServerAdvancedMess.parse(responseLine);
        }
    }

    private void updateLocalState(ServerAdvancedMess response) {
        if (!response.boardMessage().isBlank()) {
            board.updateBoard(response.boardMessage());
        }
        hash = response.hash();
        nonce = response.nonce();
        creationTime = response.creationTime();
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
            case TIMEOUT -> System.out.println("Your time is up!");
            case REPLAY -> System.out.println("You replayed!");
        }
    }

    private boolean isTerminal(GameState state) {
        return state == GameState.END
            || state == GameState.WIN
            || state == GameState.LOST
            || state == GameState.DRAW
            || state == GameState.TIMEOUT
            || state == GameState.REPLAY;
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
        if (args.length > 2){
            port = Integer.parseInt(args[2]);
        }

        new ReplayCheatClient(host, port, turnStart).start();
    }

}
