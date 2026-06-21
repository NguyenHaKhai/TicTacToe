package vgu.hihi.ttt.basic.svclarchitecture.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import vgu.hihi.ttt.basic.Board;
import vgu.hihi.ttt.basic.Board2D;
import vgu.hihi.ttt.basic.BoardFormatter;
import vgu.hihi.ttt.basic.GameState;
import vgu.hihi.ttt.basic.settings.Constant;

/**
 * Stateless client using a one-request-per-turn protocol.
 * The server sends the official initial board after the start request.
 * START|1 for human to start
 * START|2 for computer to start
 */
public class HttpTicTacToeClient {
    private final String host;
    private final int port;
    private final String turnStart;
    private final Board board;
    private final Scanner scanner;
    private final HttpClient client;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public HttpTicTacToeClient() {
        this(Constant.DEFAULT_HOST, Constant.DEFAULT_PORT, Constant.DEFAULT_START);
    }

    public HttpTicTacToeClient(String host, int port, String turnStart) {
        this.host = host;
        this.port = port;
        this.turnStart = turnStart;
        this.board = new Board2D();
        this.scanner = new Scanner(System.in);
        this.client = HttpClient.newHttpClient();
    }

    public void start() {
        System.out.println("========== TIC-TAC-TOE ==========");
        System.out.println("Connected mode: stateless TCP request/response");

        try {
            ServerDumbMess response = sendMessage(new ClientDumbMess("START", turnStart));
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
            BoardFormatter.formatSymbols(board);
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
        BoardFormatter.formatSymbols(board);
    }

    private ServerDumbMess sendOneTurn(String moveText) throws IOException {
        return sendMessage(new ClientDumbMess(moveText, board.toMessage()));
    }

    private ServerDumbMess sendMessage(ClientDumbMess request) throws IOException {
        // try (Socket socket = new Socket(host, port);
        //      BufferedReader input = new BufferedReader(
        //         new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
        //      );
        //      PrintWriter output = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

        //     output.println(request.toProtocolMessage());

        //     String responseLine = input.readLine();
        //     if (responseLine == null) {
        //         throw new IOException("server closed the connection without a response");
        //     }

        //     return ServerDumbMess.parse(responseLine);
        // }
        String jsonPayload = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://" + host + ":" + port + "/move"))
                .header("Content-Type", "application/json") // Define the header
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload)) // Attach the body payload
                .build();
        
        try {
            HttpResponse<InputStream> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

            int status = httpResponse.statusCode();

            if(status != 200){
                throw new IOException("Server returned HTTP " + status);
            }

            try (InputStream stream = httpResponse.body()) {
                // Jackson parses the network stream bytes on-the-fly directly into your record
                ServerDumbMess data = objectMapper.readValue(stream, ServerDumbMess.class);
                return data;
            }

        } catch (IOException e) {
            throw new IOException("Something went wrong in sendMessage()", e);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
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
        String turnStart = Constant.DEFAULT_START;
        String host = Constant.DEFAULT_HOST;
        int port = Constant.DEFAULT_PORT;

        if (args.length > 0) {
            turnStart = args[0];
            if(!turnStart.equals(String.valueOf(Constant.HUMAN_ID))
                && !turnStart.equals(String.valueOf(Constant.COMPUTER_ID))){
                System.out.println("Usage: 1 for human first, 2 for computer first");
                return;
            }
        }
        if (args.length > 1) {
            host = args[1];
        }
        if (args.length > 2) {
            port = Integer.parseInt(args[2]);
        }

        new HttpTicTacToeClient(host, port, turnStart).start();
    }

}
