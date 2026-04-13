package vgu.hihi.ttt.basic;

/**
 * This is when main function resides, and I will update 
 * my project day by day. Hope that I get to create a better design!
 *
 */
public class App 
{
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Please, input a valid option [1-2]");
            return;
        }

        int turn;
        try {
            turn = Integer.parseInt(args[0]);
        if(turn != 1 && turn != 2){
            System.out.println("Please, input a valid option [1-2]");
            return;
        }
        } catch (NumberFormatException e) {
            System.out.println("Please, input a valid option [1-2]");
            return;
        }
        

        // Create a board, initialize and print it out
        Board board = new Board2D(4,5);
        //board.initializeBoard();
        System.out.println("Hello!");
        board.printBoard();

        // Create two players: a human and a computer
        // Scanner scanner = new Scanner(System.in);
        Player Khai = new HumanPlayer(1);
        Player Bot = new ComputerPlayer(2);
        // // test play with 3 players
        // Player Bot2 = new ComputerPlayer(3);
        
        while(turn != 0){
            int moveMade = 0;
            if(turn == 1){
                moveMade = Khai.makeMove(board);
                if(moveMade != -1){
                    board.setCell(moveMade, Khai.getId());
                    turn = Bot.getId();
                }
                else System.out.println("Cell is not valid! Try again.");
            }
            else if(turn == 2){
                moveMade = Bot.makeMove(board);
                if(moveMade != -1){
                    board.setCell(moveMade, Bot.getId());
                    turn = Khai.getId();
                }
            }
            else System.out.println("Invalid int value: Check again!"); //optional, since handled at the start already          
            
            
            // case 3:
            //     Bot2.makeMove(board);
            //     break;

            // // assume that turn goes in a cycle: 1 -> 2 -> 3 -> 1 -> 2 -> 3 -> 1 -> ...
            // turn = ++turn > 3 ? 1 : turn;

            board.printBoard();
            System.out.println("");

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

            // turn = (turn == 1) ? 2 : 1;
        }

        // scanner.close();
    }
}
// while (true) {
//             int totalCell = board.getSize();
//             System.out.print("Enter cell number (1-" 
//             + totalCell + "): ");
//             move = scanner.nextInt();
//             if (!board.isCellOutOfBound(move - 1) && board.isCellEmpty(move - 1)) {
//                 board.setCell(move - 1, playerId);
//                 break;
//             } else {
//                 System.out.println("Cell is not empty! Try again.");
//             }
//         }
