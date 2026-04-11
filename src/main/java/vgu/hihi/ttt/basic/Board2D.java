package vgu.hihi.ttt.basic;

import java.io.PrintStream;

public class Board2D extends Board{

    private int[][] status;
    
    // constructor
    Board2D(int r, int c){
        row = r;
        col = c;
        status = new int[r][c];
        printer = new PrintStream(System.out);
    }

    Board2D(){
        row = 3;
        col = 3;
        status = new int[3][3];
        printer = new PrintStream(System.out);
    }

    @Override
    public void printBoard(){
        // String result = "";
        for (int i = 0; i < getSize(); i++) {
            printer.print("| " + getCell(i) + " ");
            if ((i + 1) % getCol() == 0) {
                printer.print("|\n");
            }
        }
        // result += "\n";
        // return result;
    }

    @Override
    public int checkWinner3(){
        // check rows
        for (int r = 0; r < row; r++) {
            for (int c = 0; c <= col - 3; c++) { // only start positions that allow 3 in a row
                // int first = status[r * col + c];
                    // status[r * col + c + 1] == first &&
                    // status[r * col + c + 2] == first
                int first = status[r][c];
                if (first != 0 &&
                    status[r][c + 1] == first &&
                    status[r][c + 2] == first) {
                    return first;
                }
            }
        }

        // check columns
        for (int c = 0; c < col; c++) {
            for (int r = 0; r <= row - 3; r++) {
                // int first = status[r * col + c];
                // status[(r + 1) * col + c] == first &&
                //     status[(r + 2) * col + c] == first
                int first = status[r][c];
                if (first != 0 &&
                    status[r + 1][c] == first &&
                    status[r + 2][c] == first) {
                    return first;
                }
            }
        }

        // check diagonals (top-left → bottom-right)
        for (int r = 0; r <= row - 3; r++) {
            for (int c = 0; c <= col - 3; c++) {
                // int first = status[r * col + c];
                // status[(r + 1) * col + (c + 1)] == first &&
                //     status[(r + 2) * col + (c + 2)] == first
                int first = status[r][c];
                if (first != 0 &&
                    status[r + 1][c + 1] == first &&
                    status[r + 2][c + 2] == first) {
                    return first;
                }
            }
        }

        // check anti-diagonals (top-right → bottom-left)
        for (int r = 0; r <= row - 3; r++) {
            for (int c = 2; c < col; c++) { // start from col 2 to col-1
                // int first = status[r * col + c];
                // status[(r + 1) * col + (c - 1)] == first &&
                //     status[(r + 2) * col + (c - 2)] == first
                int first = status[r][c];
                if (first != 0 &&
                    status[r + 1][c - 1] == first &&
                    status[r + 2][c - 2] == first) {
                    return first;
                }
            }
        }

        return 0; // no winner
    }

    @Override
    public boolean isCellEmpty(int position){
        // if (position < 0 || position >= row * col) {
        //     System.out.println("Player entered out of bound position!");
        //     return false; // or throw exception
        // }
        // x * col + y = position
        return status[position / col][position % col] == 0;
    }

    @Override
    public int getCell(int position){
        return status[position / col][position % col];
    }

    @Override
    public void setCell(int position, int playerId){
        status[position / col][position % col] = playerId;
    }

    @Override
    public boolean isFull() {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                if(status[i][j] == 0) return false;
            }
        }
        return true;
    }
}