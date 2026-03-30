package vgu.hihi.ttt.basic;

public class ComputerPlayer extends Player {

    public ComputerPlayer(int id) {
        super(id);
    }

    @Override
    public void makeMove(Board2D board) {
        // simple strategy: pick first empty cell
        for (int i = 0; i < board.getSize(); i++) {
            if (board.isCellEmpty(i)) {
                board.setCell(i, playerId);
                System.out.println("Computer chooses cell " + (i + 1));
                break;
            }
        }
    }
}