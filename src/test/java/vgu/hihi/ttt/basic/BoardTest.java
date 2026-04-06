package vgu.hihi.ttt.basic;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Board object
 */
class BoardTest{
    //private Board testBoard = new Board(4,5);

    // Test isFull() method: board is full
    @Test 
    void isFullTrue() {
        Board testBoard = new Board(3, 3, new int[]{1,2,2,1,2,2,1,2,2});

        assertTrue(testBoard.isFull());
    }

    // Test isFull() method: board is not full
    @Test
    void isFullFalse() {
        Board testBoard = new Board(3, 3, new int[]{1,2,2,1,2,0,1,2,2});

        assertFalse(testBoard.isFull());
    }

    // Test isCellEmpty() method: cell is empty
    @Test
    void cellEmptyTrue() {
        Board testBoard = new Board(3, 3, new int[]{1,2,2,1,2,0,1,2,2});

        assertTrue(testBoard.isCellEmpty(5));
    }

    // Test isCellEmpty() method: cell is not empty
    @Test
    void cellEmptyFalse() {
        Board testBoard = new Board(3, 3, new int[]{1,2,2,1,2,0,1,2,2});

        assertFalse(testBoard.isCellEmpty(0));
    }

    // Test isCellOutOfBound() method: cell is out of bound > size of the board
    @Test
    void cellOutOfBoundgtSizeTrue() {
        Board testBoard = new Board(3, 3, new int[]{1,2,2,1,2,0,1,2,2});

        assertTrue(testBoard.isCellOutOfBound(100));
    }

    // Test isCellOutOfBound() method: cell is out of bound < 0
    @Test
    void cellOutOfBoundltZeroTrue() {
        Board testBoard = new Board(3, 3, new int[]{1,2,2,1,2,0,1,2,2});

        assertTrue(testBoard.isCellOutOfBound(-1));
    }
    
    // Test isCellOutOfBound() method: cell is not out of bound
    @Test
    void cellOutOfBoundltFalse() {
        Board testBoard = new Board(3, 3, new int[]{1,2,2,1,2,0,1,2,2});

        assertFalse(testBoard.isCellOutOfBound(3));
    }

    // test checkWinner3() method: player wins anti diagonal
    @Test
    void winnerADTrue(){
        Board testBoard = new Board(3, 3, new int[]{1,2,1,0,1,0,1,2,2});

        assertEquals(testBoard.checkWinner3(), 1);
    }

    // test checkWinner3() method: player wins row
    @Test
    void winnerRTrue(){
        Board testBoard = new Board(3, 3, new int[]{1,2,1,2,2,2,1,1,2});

        assertEquals(testBoard.checkWinner3(), 2);
    }

    // test checkWinner3() method: player wins col
    @Test
    void winnerCTrue(){
        Board testBoard = new Board(3, 3, new int[]{1,2,1,1,2,2,1,1,2});

        assertEquals(testBoard.checkWinner3(), 1);
    }

    // test checkWinner3() method: player wins diagonal
    @Test
    void winnerDTrue(){
        Board testBoard = new Board(3, 3, new int[]{1,2,1,1,1,2,2,1,1});

        assertEquals(testBoard.checkWinner3(), 1);
    }

    // test checkWinner3() method: draw
    @Test
    void drawTrue(){
        Board testBoard = new Board(3, 3, new int[]{1,2,1,1,2,2,2,1,1});

        assertEquals(testBoard.checkWinner3(), 0);
    }

    // test printBoard() method: initialization board
    @Test
    void printBoardITrue(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Board testBoard = new Board(out);
        String nl = System.lineSeparator();
        String boardRep = "| 0 | 0 | 0 |" + nl + "| 0 | 0 | 0 |" + nl + "| 0 | 0 | 0 |" + nl;

        testBoard.printBoard();
        assertEquals(boardRep, out.toString());
    }

    // test printBoard() method: custom board
    @Test
    void printBoardCTrue(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String nl = System.lineSeparator();

        Board testBoard = new Board(3, 4, new int[]{1,2,1,1,2,2,2,1,1,1,2,1}, out);
        String boardRep = "| 1 | 2 | 1 | 1 |" + nl + "| 2 | 2 | 2 | 1 |" + nl + "| 1 | 1 | 2 | 1 |" + nl;

        testBoard.printBoard();
        assertEquals(boardRep, out.toString());
    }
}
