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
        String data = scanner.nextLine();
        if(isInteger(data)){
            move = Integer.parseInt(data);
            if (!board.isCellOutOfBound(move - 1)){
                if(board.isCellEmpty(move - 1)) return move - 1;
                else return -2; // signal occupied cell
            } 
            else return -1;
        }
        else if(data.equals("q")) return -3; // handle end game
        else return -1;
    }

    private boolean isInteger(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    try {
        Integer.parseInt(str);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}

}