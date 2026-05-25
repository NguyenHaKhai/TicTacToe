package vgu.hihi.ttt.basic.svclarchitecture;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import vgu.hihi.ttt.basic.Board;
import vgu.hihi.ttt.basic.Board2D;
import vgu.hihi.ttt.basic.ComputerPlayer;
import vgu.hihi.ttt.basic.Game;
import vgu.hihi.ttt.basic.HumanPlayer;
import vgu.hihi.ttt.basic.Player;

public class ServerType1 {
    private static final int PORT = 1234;

    private ServerSocket serverSocket;

    public ServerType1() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port " + PORT);
        System.out.println("Waiting for client connection...");
    }

    public void start() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected!");

                handleClient(clientSocket);

            } catch (IOException e) {
                System.err.println("Server error: " + e.getMessage());
            }
        }
    }

    private void handleClient(Socket clientSocket) throws IOException {
        // PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
        // BufferedReader clientIn = new BufferedReader(
        //         new InputStreamReader(clientSocket.getInputStream())
        // );

        // SocketInputBuffer humanInputBuffer = new SocketInputBuffer();
        // PrintStream clientPrintStream = SocketPrintStreamFactory.from(clientOut);

        // Thread socketReaderThread = new Thread(() -> {
        //     try {
        //         String line;

        //         while ((line = clientIn.readLine()) != null) {
        //             humanInputBuffer.submitLine(line);
        //         }
        //     } catch (IOException e) {
        //         System.err.println("Client disconnected: " + e.getMessage());
        //     } finally {
        //         humanInputBuffer.closeBuffer();
        //     }
        // });
        

        // socketReaderThread.setDaemon(true);
        // socketReaderThread.start();
        PrintStream clientOut = new PrintStream(clientSocket.getOutputStream(), true);

        // PrintStream clientPrintStream = SocketPrintStreamFactory.from(clientOut);

        clientOut.println("========== TIC-TAC-TOE ==========");
        clientOut.println("Game initialized! Starting...");

        int startTurn = 1;

        Board board = new Board2D();
        board.setPrinter(clientOut);
        board.printBoard();


        Player human = new HumanPlayer(1, clientSocket.getInputStream());
        Player computer = new ComputerPlayer(2);

        Game game = new Game(board, human, computer, startTurn, clientOut);
        game.play();

        clientOut.println("Connection closed by server.");
    }

    public static void main(String[] args) {
        try {
            ServerType1 server = new ServerType1();
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}