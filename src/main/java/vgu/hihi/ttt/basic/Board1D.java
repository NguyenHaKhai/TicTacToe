package vgu.hihi.ttt.basic;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Board1D extends Board{
    
    private int[] status;
    // consider adding PrintStream, then in construction
    // define something like printer = new PrintStream(out);

    // Constructors for board
    public Board1D(int r, int c){
        row = r;
        col = c;
        status = new int[row * col];
        printer = new PrintStream(System.out);
    }
    public Board1D(){
        row = 3;
        col = 3;
        status = new int[row * col];
        printer = new PrintStream(System.out);
    }
    // Constructor for testing
    public Board1D(int r, int c, int[] desiredState){
        row = r;
        col = c;
        status = new int[row * col];
        // printer = new PrintStream(out);
        if((row * col) == desiredState.length){
            System.arraycopy(desiredState, 0, status, 0, desiredState.length);
        }
        else{
            System.out.println("Incompatible Size: not copy contents");
        }
    }

    public Board1D(int r, int c, ByteArrayOutputStream out){
        row = r;
        col = c;
        status = new int[row * col];
        printer = new PrintStream(out);
    }
    public Board1D(ByteArrayOutputStream out){
        row = 3;
        col = 3;
        status = new int[row * col];
        printer = new PrintStream(out);
    }

    public Board1D(int r, int c, int[] desiredState, ByteArrayOutputStream out){
        row = r;
        col = c;
        status = new int[row * col];
        printer = new PrintStream(out);
        if((row * col) == desiredState.length){
            System.arraycopy(desiredState, 0, status, 0, desiredState.length);
        }
        else{
            System.out.println("Incompatible Size: not copy contents");
        }
    }

    // ! may be depreacated
    // public Board1D(int r, int c, String messBoard){
    //     // for the cl-sv type 3
        
    // }

    // public void initializeBoard(){
    //     for(int i = 0; i < row * col; i++){
    //         status[i] = 0;
    //     }
    // }

    // Here we assume the win mode is 3 consecutive symbols
    @Override
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
    @Override
    public boolean isCellEmpty(int position){
        // if (position < 0 || position >= row * col) {
        //     System.out.println("Player entered out of bound position!");
        //     return false; // or throw exception
        // }
        return status[position] == 0;
    }
    
    // public boolean isCellOutOfBound(int position){
    //     return (position < 0 || position >= row * col);
    // }

    @Override
    public int getCell(int position){
        return status[position];
    }

    @Override
    public void setCell(int position, int playerId){
        status[position] = playerId;
    }

    @Override
    public boolean isFull() {
        for (int cell : status) {
            if (cell == 0) return false;
        }
        return true;
    }

}

