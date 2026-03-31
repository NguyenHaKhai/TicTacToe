package vgu.hihi.ttt.basic;

public class Board{
    
    private int row;
    private int col;
    private int[] status;

    // Constructors for board
    public Board(int r, int c){
        row = r;
        col = c;
        status = new int[row * col];
    }
    public Board(){
        row = 3;
        col = 3;
        status = new int[row * col];
    }
    // Constructor for testing
    public Board(int r, int c, int[] desiredState){
        row = r;
        col = c;
        status = new int[row * col];
        if((row * col) == desiredState.length){
            System.arraycopy(desiredState, 0, status, 0, desiredState.length);
        }
        else{
            System.out.println("Incompatible Size: not copy contents");
        }
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

    // public void initializeBoard(){
    //     for(int i = 0; i < row * col; i++){
    //         status[i] = 0;
    //     }
    // }

    public void printBoard(){
        for (int i = 0; i < getSize(); i++) {
            System.out.print("| " + status[i] + " ");
            if ((i + 1) % getCol() == 0) {
                System.out.println("|");
            }
        }
        System.out.println();
    }

    // Here we assume the win mode is 3 consecutive symbols
    public int checkWinner3() {
        // check rows
        for (int r = 0; r < row; r++) {
            for (int c = 0; c <= col - 3; c++) { // only start positions that allow 3 in a row
                int first = status[r * col + c];
                if (first != 0 &&
                    status[r * col + c + 1] == first &&
                    status[r * col + c + 2] == first) {
                    return first;
                }
            }
        }

        // check columns
        for (int c = 0; c < col; c++) {
            for (int r = 0; r <= row - 3; r++) {
                int first = status[r * col + c];
                if (first != 0 &&
                    status[(r + 1) * col + c] == first &&
                    status[(r + 2) * col + c] == first) {
                    return first;
                }
            }
        }

        // check diagonals (top-left → bottom-right)
        for (int r = 0; r <= row - 3; r++) {
            for (int c = 0; c <= col - 3; c++) {
                int first = status[r * col + c];
                if (first != 0 &&
                    status[(r + 1) * col + (c + 1)] == first &&
                    status[(r + 2) * col + (c + 2)] == first) {
                    return first;
                }
            }
        }

        // check anti-diagonals (top-right → bottom-left)
        for (int r = 0; r <= row - 3; r++) {
            for (int c = 2; c < col; c++) { // start from col 2 to col-1
                int first = status[r * col + c];
                if (first != 0 &&
                    status[(r + 1) * col + (c - 1)] == first &&
                    status[(r + 2) * col + (c - 2)] == first) {
                    return first;
                }
            }
        }

        return 0; // no winner
    }

    // void updateBoard(int symbol, int position){
    //     status[position] = symbol;
    // }

    // consider breaking this into 2 methods: out of boudn and isOccupied
    public boolean isCellEmpty(int position){
        // if (position < 0 || position >= row * col) {
        //     System.out.println("Player entered out of bound position!");
        //     return false; // or throw exception
        // }
        return status[position] == 0;
    }
    
    public boolean isCellOutOfBound(int position){
        return (position < 0 || position >= row * col);
    }

    public int getCell(int position){
        return status[position];
    }

    public void setCell(int position, int playerId){
        status[position] = playerId;
    }

    public boolean isFull() {
        for (int cell : status) {
            if (cell == 0) return false;
        }
        return true;
    }

    // // copy board for testing
    // public void copyBoard(int[] statusContents){
    //     if(this.getSize() != statusContents.length){
    //         System.out.println("Incompatible Size");
    //         return;
    //     }
    //     System.arraycopy(statusContents, 0, status, 0, this.getSize());
    // }

}

