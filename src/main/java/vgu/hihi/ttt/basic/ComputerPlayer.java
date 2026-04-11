package vgu.hihi.ttt.basic;

public class ComputerPlayer extends Player {
    private int move = 1;

    public ComputerPlayer(int id) {
        super(id);
    }

    @Override
    public int makeMove(Board board) {
        // simple strategy: pick first empty cell
        int currentMove = move;
        move++;
        if (!board.isCellOutOfBound(currentMove - 1) && board.isCellEmpty(currentMove - 1)) return currentMove - 1;
        else return -1;
    }
}