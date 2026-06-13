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

    public PrintStream getPrinter() {
        return printer;
    }

    public void setPrinter(PrintStream printer) {
        this.printer = printer;
    }

    public String printBoard(){
        String result = "";
        for (int i = 0; i < getSize(); i++) {
            printer.print("| " + getCell(i) + " ");

            // for the cl-sv type 1, 2
            result += "| " + getCell(i) + " ";
            if ((i + 1) % getCol() == 0) {
                printer.print("|\n");

                // for the cl-sv type 1, 2
                result += "|\n";
            }
        }
        // result += "\n";
        return result;
    }

    public String toMessage(){ // to convert to message exchanged between cl & sv type 3
        String message = "";
        for(int i = 0; i < getSize(); i++){
            message += getCell(i) + " ";
        }
        return message.trim(); // trim the last white space
    }

    public void updateBoard(String messBoard){ // use for update the board according to the message in sv-cl
        // task: parse the message string of the board to update status
        if (messBoard == null) {
            throw new IllegalArgumentException("Board message must not be null");
        }

        String[] cells = messBoard.trim().split("\\s+");
        if (cells.length != getSize()) {
            throw new IllegalArgumentException(
                "Expected " + getSize() + " board cells, received " + cells.length
            );
        }

        for(int i = 0; i < getSize(); i++){
            int cellValue = Integer.parseInt(cells[i]);
            if (cellValue < 0 || cellValue > 2) {
                throw new IllegalArgumentException("Unsupported board cell value: " + cellValue);
            }
            setCell(i, cellValue);
        }
    }

    public abstract int checkWinner3();
    public abstract boolean isCellEmpty(int position);
    public abstract int getCell(int position);
    public abstract void setCell(int position, int playerId);
    public abstract boolean isFull();
}

