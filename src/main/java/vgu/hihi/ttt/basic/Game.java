package vgu.hihi.ttt.basic;

import java.io.PrintStream;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private int turn;
    private PrintStream out;


    public Game(Board board, Player player1, Player player2, int turn){
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = turn;
        this.out = System.out;
    }

    public Game(Board board, Player player1, Player player2, int turn, PrintStream out) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = turn;
        this.out = out;
    }

    public void play(){
        // Thread gameThread = new Thread(() -> {
        
            while(turn != 0){
            int moveMade;
            if(turn == player1.getId()){
                out.println("Player#" + player1.getId() + "'s turn");
                moveMade = player1.makeMove(board); 
                GameState currentState = GameLogic.applyMove(board, player1, moveMade);
                switch(currentState){
                    case GameState.END -> {
                        out.println("End of the game");
                        turn = 0;
                    }
                    case GameState.OCCUPIED -> out.println("The cell is occupied!");
                    case GameState.INVALID -> out.println("Please, input a valid number [1-9]");
                    case GameState.WIN -> {
                        board.printBoard();
                        out.println("Player#" + player1.getId() + " won!");
                        turn = 0;
                    }
                    case GameState.DRAW -> {
                        board.printBoard();
                        out.println("It is a draw!");
                        turn = 0;
                    }
                    case GameState.CONT -> {
                        board.printBoard();
                        turn = player2.getId();
                    }
                    default -> out.println("Something is not correct. Game state: " + currentState);
                }
            }
            // here I tried to make it abstract, yet we have the fact that a computer dont
            // output Player#2 turn when it makes wrong move -> move the line into moveMade if
            else if(turn == player2.getId()){
                moveMade = player2.makeMove(board);
                GameState currentState = GameLogic.applyMove(board, player2, moveMade);
                switch(currentState){
                    case GameState.INVALID -> {}
                    case GameState.WIN -> {
                        out.println("Player#" + player2.getId() + "'s turn"); // this is placed here because only prints this when computer makes the valid move
                        board.printBoard();
                        out.println("Player#" + player2.getId() + " won!");
                        turn = 0;
                    }
                    case GameState.DRAW -> {
                        out.println("Player#" + player2.getId() + "'s turn"); // this is placed here because only prints this when computer makes the valid move
                        board.printBoard();
                        out.println("It is a draw!");
                        turn = 0;
                    }
                    case GameState.CONT -> {
                        out.println("Player#" + player2.getId() + "'s turn"); // this is placed here because only prints this when computer makes the valid move
                        board.printBoard();
                        turn = player1.getId();
                    }
                    default -> out.println("Something is not correct. Game state: " + currentState);
                }
            }
            else{
                out.println("Invalid int value: Check again!"); //optional, since handled at the start already
            }
        }
        // });
        // gameThread.start();
        
            
    }
}
