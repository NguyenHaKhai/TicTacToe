package vgu.hihi.ttt.basic;

import java.io.InputStream;
import java.util.Scanner;

public class HumanPlayer extends Player{
    final private Scanner scanner;

    public HumanPlayer(int id, InputStream in ) {
        super(id);
        this.scanner = new Scanner(in);
    }

    @Override
    public void makeMove(Board board) {
        int move;
        while (true) {
            int totalCell = board.getSize();
            System.out.print("Enter cell number (1-" 
            + totalCell + "): ");
            move = scanner.nextInt();
            if (board.isCellEmpty(move - 1) && !board.isCellOutOfBound(move - 1)) {
                board.setCell(move - 1, playerId);
                break;
            } else {
                System.out.println("Cell is not empty! Try again.");
            }
        }
    }

}