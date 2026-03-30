package vgu.hihi.ttt.basic;

public class Board2D{

    private int row;
    private int col;
    private int[][] status;
    
    // constructor
    Board2D(int r, int c){
        row = r;
        col = c;
        status = new int[r][c];
    }

    Board2D(){
        row = 3;
        col = 3;
        status = new int[3][3];
    }

    // basic getters, setters
    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public int getSize(){
        return row * col;
    }

    public void printBoard(){
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                System.out.print("| " + status[i][j] + " ");
            }
            System.out.println("|");
        }
    }

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

    public boolean isCellEmpty(int position){
        if (position < 0 || position >= row * col) {
            System.out.println("Player entered out of bound position!");
            return false; // or throw exception
        }
        // x * col + y = position
        return status[position / col][position % col] == 0;
    }

    public void setCell(int position, int playerId){
        status[position / col][position % col] = playerId;
    }

    public boolean isFull() {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                if(status[i][j] == 0) return false;
            }
        }
        return true;
    }
}