package vgu.hihi.ttt.basic;

import java.io.InputStream;
import java.util.Scanner;

public class HumanPlayer extends Player{
    final private Scanner scanner;

    public HumanPlayer(int id ) {
        super(id);
        this.scanner = new Scanner(System.in);
    }
    // testing
    public HumanPlayer(int id, InputStream in ) {
        super(id);
        this.scanner = new Scanner(in);
    }

    // note: use readline, refactor makeMove: not include System.out, avoid using methods from Board
    // idea: move most of the code to App.java
    // important: check for cellOfBound first
    @Override
    public int makeMove(Board board) {
        int move;
        int totalCell = board.getSize();
            System.out.print("Enter cell number (1-" 
            + totalCell + "): ");
        if(scanner.hasNextInt()){
            move = scanner.nextInt();
            if (!board.isCellOutOfBound(move - 1) && board.isCellEmpty(move - 1)) return move - 1;
            else return -1;
        }
        else return -1;
    }

}