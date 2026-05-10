package vgu.hihi.ttt.basic;

/**
 * This is when main function resides, and I will update 
 * my project day by day. Hope that I get to create a better design!
 *
 */
public class App 
{
    public static void main(String[] args) {
        // consider putting the game inside another thread to test the behaviour: otw it will freeze waiting for the while loop to finish
        // consider putting the logic inside another java file: Game or GameLogic
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
        // temp
        

        // Create a board, initialize and print it out
        try {
            Board board = new Board2D();
            //board.initializeBoard();
            System.out.println("Hello!");
            board.printBoard();

            
            // Create two players: a human and a computer
            // Scanner scanner = new Scanner(System.in);
            Player Khai = new HumanPlayer(1);
            Player Bot = new ComputerPlayer(2);
            // // test play with 3 players
            // Player Bot2 = new ComputerPlayer(3);
            Game game = new Game(board, Khai, Bot, turn);
            game.play();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Broken Pipe Simulation")) {
                System.err.println("Detected broken pipe. Exiting gracefully...");
                return;
            }
            throw e;
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


// the second and third last not done, the fourth last will be done by lecturer