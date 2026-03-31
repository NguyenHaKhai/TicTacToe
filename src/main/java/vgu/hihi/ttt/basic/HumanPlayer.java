package vgu.hihi.ttt.basic;

import java.util.Scanner;

public class HumanPlayer extends Player{
    final private Scanner scanner;

    public HumanPlayer(int id, Scanner scanner) {
        super(id);
        this.scanner = scanner;
    }

    @Override
    public void makeMove(Board board) {
        int move;
        while (true) {
            int totalCell = board.getSize();
            System.out.print("Enter cell number (1-" 
            + totalCell + "): ");
            move = scanner.nextInt();
            if (board.isCellEmpty(move - 1)) {
                board.setCell(move - 1, playerId);
                break;
            } else {
                System.out.println("Cell is not empty! Try again.");
            }
        }
    }

}