package vgu.hihi.ttt.basic;

import java.io.PrintStream;

public abstract class Board {
    protected int row;
    protected int col;

    protected PrintStream printer;

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public int getSize(){
        return row * col;
    }

    public boolean isCellOutOfBound(int position){
        return (position < 0 || position >= row * col);
    }

    public void setPrinter(PrintStream printer) {
        this.printer = printer;
    }


    public abstract String printBoard();
    public abstract int checkWinner3();
    public abstract boolean isCellEmpty(int position);
    public abstract int getCell(int position);
    public abstract void setCell(int position, int playerId);
    public abstract boolean isFull();
}

