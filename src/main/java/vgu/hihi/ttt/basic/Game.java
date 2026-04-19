package vgu.hihi.ttt.basic;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private int turn;

    public Game(Board board, Player player1, Player player2, int turn){
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = turn;
    }

    public void play(){
        Thread gameThread = new Thread(() -> {
            while(turn != 0){
            int moveMade;
            if(turn == player1.getId()){
                System.out.println("Player#" + player1.getId() + "'s turn");
                moveMade = player1.makeMove(board);
                    if(moveMade == -3){
                        System.out.println("End of the game");
                        turn = 0;
                    }
                    else if(moveMade == -2){
                        System.out.println("The cell is occupied!");
                    }
                    else if(moveMade != -1){
                        board.setCell(moveMade, player1.getId());
                        turn = player2.getId();
                        progress();
                    }
                    else System.out.println("Please, input a valid number [1-9]");
            }
            // here I tried to make it abstract, yet we have the fact that a computer dont
            // output Player#2 turn when it makes wrong move -> move the line into moveMade if
            else if(turn == player2.getId()){
                moveMade = player2.makeMove(board);
                    if(moveMade != -1){
                        System.out.println("Player#" + player2.getId() + "'s turn");
                        board.setCell(moveMade, player2.getId());
                        turn = player1.getId();
                        progress();
                    }
            }
            else{
                System.out.println("Invalid int value: Check again!"); //optional, since handled at the start already
            }
                
            // case 3:
            //     player22.makeMove(board);
            //     break;

            // // assume that turn goes in a cycle: 1 -> 2 -> 3 -> 1 -> 2 -> 3 -> 1 -> ...
            // turn = ++turn > 3 ? 1 : turn;

            // turn = (turn == 1) ? 2 : 1;
        }
        });
        gameThread.start();
    }

    private void progress(){
        board.printBoard();
            System.out.println("");

            int winner = board.checkWinner3();

            if (winner == player1.getId()) {
                System.out.println("Player#" + player1.getId() + " won");
                turn = 0;
            } else if (winner == player2.getId()) {
                System.out.println("Player#" + player2.getId() + " won");
                turn = 0;
            } else if (board.isFull()) {
                System.out.println("It is a draw!");
                turn = 0;
            }
    }
    // consider implement in the future when player1 is not human
    private void humanPlay(){
        int moveMade;
        System.out.println("Player#" + player1.getId() + "'s turn");
        moveMade = player1.makeMove(board);
            if(moveMade == -3){
                System.out.println("End of the game");
                turn = 0;
            }
            else if(moveMade == -2){
                System.out.println("The cell is occupied!");
            }
            else if(moveMade != -1){
                board.setCell(moveMade, player1.getId());
                turn = player2.getId();
                progress();
            }
            else System.out.println("Please, input a valid number [1-9]");
    }
    // consider implement in the future when player2 is not computer
    private void computerPlay(){
        int moveMade;
        moveMade = player2.makeMove(board);
                    if(moveMade != -1){
                        System.out.println("Player#" + player2.getId() + "'s turn");
                        board.setCell(moveMade, player2.getId());
                        turn = player1.getId();
                        progress();
                    }
    }
}
