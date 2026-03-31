package vgu.hihi.ttt.basic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Board object
 */
class BoardTest{
    //private Board testBoard = new Board(4,5);

    @Test
    void isFullTrue() {
        Board testBoard = new Board(3, 3, new int[]{1,2,2,1,2,0,1,2,2});

        assertFalse(testBoard.isFull());
    }

    
}