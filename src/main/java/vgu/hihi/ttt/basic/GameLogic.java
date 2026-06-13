package vgu.hihi.ttt.basic;

public class GameLogic {
    public static GameState applyMove(Board board, Player player, int move) {
        switch (move) {
            case -3 -> {
                return GameState.END;
            }
            case -2 -> {
                return GameState.OCCUPIED;
            }
            case -1 -> {
                return GameState.INVALID;
            }
            default -> {
                board.setCell(move, player.getId());
                // check for winner or draw after making the move
                if (board.checkWinner3() == player.getId()) return GameState.WIN;
                if (board.isFull()) return GameState.DRAW;
                return GameState.CONT;
            }
        }
    }
}
