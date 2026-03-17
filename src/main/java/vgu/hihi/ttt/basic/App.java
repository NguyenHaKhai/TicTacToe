package vgu.hihi.ttt.basic;
import java.util.Scanner;

/**
 * This is when main function resides, and I will update 
 * my project day by day. Hope that I get to create a better design!
 *
 */
public class App 
{
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java TicTacToe [1|2]");
            return;
        }

        int turn = Integer.parseInt(args[0]);

        // Create a board, initialize and print it out
        Board board = new Board(4,5);
        board.initializeBoard();
        board.printBoard();

        // Create two players: a human and a computer
        Scanner scanner = new Scanner(System.in);
        Player Khai = new HumanPlayer(1, scanner);
        Player Bot = new ComputerPlayer(2);

        while(true){
            if(turn == 1){
                Khai.makeMove(board);
            } else{
                Bot.makeMove(board);
            }

            board.printBoard();

            int winner = board.checkWinner3();

            if (winner == Khai.getId()) {
                System.out.println("Human wins! or Player " + Khai.getId() + " wins!");
                break;
            } else if (winner == Bot.getId()) {
                System.out.println("Computer wins! or Player " + Bot.getId() + " wins!");
                break;
            } else if (board.isFull()) {
                System.out.println("Draw!");
                break;
            }

            turn = (turn == 1) ? 2 : 1;
        }

        scanner.close();
    }
}
