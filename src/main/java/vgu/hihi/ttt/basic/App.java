package vgu.hihi.ttt.basic;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    static int[] board = new int[9];

    public static void printBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.print("| " + board[i] + " ");
            if ((i + 1) % 3 == 0) {
                System.out.println("|");
            }
        }
        System.out.println();
    }

    public static boolean isFull() {
        for (int cell : board) {
            if (cell == 0) return false;
        }
        return true;
    }

    public static int checkWinner() {
        int[][] winPatterns = {
                {0,1,2},{3,4,5},{6,7,8}, // rows
                {0,3,6},{1,4,7},{2,5,8}, // cols
                {0,4,8},{2,4,6}          // diagonals
        };

        for (int[] pattern : winPatterns) {
            if (board[pattern[0]] != 0 &&
                board[pattern[0]] == board[pattern[1]] &&
                board[pattern[1]] == board[pattern[2]]) {
                return board[pattern[0]];
            }
        }

        return 0;
    }

    public static void computerMove() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) {
                board[i] = 2;
                System.out.println("Computer chooses cell " + (i + 1));
                break;
            }
        }
    }

    public static void humanMove(Scanner scanner) {
        int move;

        while (true) {
            System.out.print("Enter cell number (1-9): ");
            move = scanner.nextInt();

            if (move >= 1 && move <= 9 && board[move - 1] == 0) {
                board[move - 1] = 1;
                break;
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java TicTacToe [1|2]");
            return;
        }

        int turn = Integer.parseInt(args[0]);
        Scanner scanner = new Scanner(System.in);

        printBoard();

        while (true) {

            if (turn == 1) {
                humanMove(scanner);
            } else {
                computerMove();
            }

            printBoard();

            int winner = checkWinner();

            if (winner == 1) {
                System.out.println("Human wins!");
                break;
            } else if (winner == 2) {
                System.out.println("Computer wins!");
                break;
            } else if (isFull()) {
                System.out.println("Draw!");
                break;
            }

            turn = (turn == 1) ? 2 : 1;
        }

        scanner.close();
    }
}
