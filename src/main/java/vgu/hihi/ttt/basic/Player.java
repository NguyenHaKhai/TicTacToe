package vgu.hihi.ttt.basic;

public abstract class Player{

    // This acts also as the symbol player input on the board
    protected int playerId;

    public Player(int id) {
        this.playerId = id;
    }

    public int getId(){
        return playerId;
    }

    public abstract int makeMove(Board board);
}