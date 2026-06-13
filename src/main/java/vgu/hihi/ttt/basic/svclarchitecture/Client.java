package vgu.hihi.ttt.basic.svclarchitecture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * CLIENT: Thin UI layer with NO game logic.
 * Responsibilities:
 * - Connect to server
 * - Display all messages from server
 * - Read user input (moves and menu choices)
 * - Send user input to server
 * 
 * The client is NEVER responsible for:
 * - Validating moves
 * - Modifying the board
 * - Determining game state
 * - AI/Computer player logic
 */
public class Client {
    private Socket socket;
    private PrintWriter serverOut;
    private BufferedReader serverIn;
    private BufferedReader userInput;

    public Client() throws IOException {
        // Connect to server
        socket = new Socket(Constant.DEFAULT_HOST, Constant.DEFAULT_PORT);
        System.out.println("✅ Connected to server at " + Constant.DEFAULT_HOST + ":" + Constant.DEFAULT_PORT);

        // Setup I/O streams
        serverOut = new PrintWriter(socket.getOutputStream(), true);
        serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        userInput = new BufferedReader(new InputStreamReader(System.in));
    }

    public void play() throws IOException {
        String serverMessage;

        // Read and display messages from server in a loop
        while ((serverMessage = serverIn.readLine()) != null) {
            System.out.println(serverMessage);

            // If server asks for input, prompt user and send response
            if (serverMessage.contains("Player#1's turn")) {
                String userMove = userInput.readLine();
                if (userMove != null) {
                    serverOut.println(userMove);
                }
            }
        }

        System.out.println("❌ Connection closed by server.");
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (userInput != null) {
            userInput.close();
        }
    }

    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client();
            client.play();
        } catch (IOException e) {
            System.err.println("❌ Client error: " + e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    System.err.println("Error closing client: " + e.getMessage());
                }
            }
        }
    }
}
