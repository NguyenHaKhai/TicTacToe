package vgu.hihi.ttt.basic.svclarchitecture.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import vgu.hihi.ttt.basic.Board;
import vgu.hihi.ttt.basic.Board2D;
import vgu.hihi.ttt.basic.ComputerPlayer;
import vgu.hihi.ttt.basic.GameLogic;
import vgu.hihi.ttt.basic.GameState;
import vgu.hihi.ttt.basic.HumanPlayer;
import vgu.hihi.ttt.basic.Player;
import vgu.hihi.ttt.basic.settings.Constant;

/**
 * Stateless server using a one-request-per-turn protocol.
 * The client starts with "START|[who moves first]", then the server creates and returns the
 * official initial board. Later requests send "move|board".
 * START|1 for human to start
 * START|2 for computer to start
 */
public class HttpTicTacToeServer {
    private final int port;
    private final HttpServer server;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public HttpTicTacToeServer() throws IOException{
        this(Constant.DEFAULT_PORT);
    }

    public HttpTicTacToeServer(int port) throws IOException {
        this.port = port;

        this.server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        // Map routing paths to specific handlers
        this.server.createContext("/move", this::handleClient);
    }

    public void start() throws IOException {
        System.out.println("HTTP server started on port " + port);
        
        // start the server
        server.start();
    }

    private void handleClient(HttpExchange exchange) throws IOException {
        try {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, new ServerDumbMess(GameState.INVALID, "Only POST requests are supported"));
                return;
            }

            // convert json to ClientDumbMess
            ClientDumbMess requestData = objectMapper.readValue(exchange.getRequestBody(), ClientDumbMess.class);
            String requestLine = requestData.toProtocolMessage();
            if (requestLine.isEmpty()) {
                sendResponse(exchange, 400, new ServerDumbMess(GameState.INVALID, "0"));
                return;
            }

            ServerDumbMess response;
            try {
                response = process(requestData);
            } catch (IllegalArgumentException e) {
                response = new ServerDumbMess(GameState.INVALID, "0");
            }

            sendResponse(exchange, 200, response);
            System.out.println(response.toProtocolMessage());
        } finally {
            exchange.close();
        }
    }

    private ServerDumbMess process(ClientDumbMess request) {
        System.out.println(request.toProtocolMessage());

        if (isStartGameRequest(request)) {
            String turnStart = request.boardMessage();
            Board newBoard = new Board2D();
            return createGame(newBoard, turnStart);
        }

        Board board = new Board2D();
        try {
            board.updateBoard(request.boardMessage());
        } catch (IllegalArgumentException e) {
            return new ServerDumbMess(GameState.INVALID, request.boardMessage());
        }

        Player human = new HumanPlayer(
            Constant.HUMAN_ID,
            new ByteArrayInputStream((request.moveText() + "\n").getBytes(StandardCharsets.UTF_8))
        );
        Player computer = new ComputerPlayer(Constant.COMPUTER_ID);

        int humanMove = human.makeMove(board);
        GameState humanMoveState = GameLogic.applyMove(board, human, humanMove);
        if(humanMoveState != GameState.CONT) {
            return responseFor(humanMoveState, board);
        }

        int computerMove = computer.makeMove(board);
        while (computerMove == -1) {
            computerMove = computer.makeMove(board); // attempt to find 1 valid move for computer
        }

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

    private boolean isStartGameRequest(ClientDumbMess request) {
        return "START".equals(request.moveText());
    }

    private ServerDumbMess createGame(Board board, String turnStart) {
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

    private ServerDumbMess responseFor(GameState state, Board board) {
        String boardMessage = board.toMessage();
        return new ServerDumbMess(state, boardMessage);
    }

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            ServerDumbMess responseBody) throws IOException {


        // specify the body type we send: json
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");

        // send the header first, then the data
        try (OutputStream outputStream = exchange.getResponseBody()) {

            // convert the object ServerDumbMess to bytes first
            byte[] jsonBytes = objectMapper.writeValueAsBytes(responseBody);

            // send the header
            // here we specify how many bytes is sent. 0 is for chunk(not known exactly how many bytes)
            // -1 is for no bytes(no payload)
            exchange.sendResponseHeaders(statusCode, jsonBytes.length);
            // send the data
            outputStream.write(jsonBytes);
        }
    }

    public static void main(String[] args) {
        int port = Constant.DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            new HttpTicTacToeServer(port).start();
        } catch (IOException e) {
            System.err.println("Failed to start HTTP server: " + e.getMessage());
        }
    }

}
