package vgu.hihi.ttt.basic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Board object
 */
class HumanPlayerTest{

    // test makeMove() method: user plays 3
    @Test
    void makeMoveTrue(){
        String input = "3" + System.lineSeparator();
        InputStream in = new ByteArrayInputStream(input.getBytes());

        HumanPlayer Khai = new HumanPlayer(1, in);
        Board testBoard = new Board1D();
        
        int result = Khai.makeMove(testBoard);

        assertEquals(result, 2);
    }

    // test makeMove() method: user plays 2: results in a invalid move(-1)
    @Test
    void makeMoveInvalidTrue(){
        String input = "2" + System.lineSeparator();
        InputStream in = new ByteArrayInputStream(input.getBytes());

        HumanPlayer Khai = new HumanPlayer(1, in);
        Board testBoard = new Board1D(3, 3, new int[]{1,2,0,2,1,2,1,1,0});

        int result = Khai.makeMove(testBoard);

        assertEquals(result, -1);
    }

    // test makeMove() method: user plays abc: results in a invalid move(-1)
    @Test
    void makeMoveInvalidCharTrue(){
        String input = "abc" + System.lineSeparator();
        InputStream in = new ByteArrayInputStream(input.getBytes());

        HumanPlayer Khai = new HumanPlayer(1, in);
        Board testBoard = new Board1D(3, 3, new int[]{1,2,0,2,1,2,1,1,0});

        int result = Khai.makeMove(testBoard);

        assertEquals(result, -1);
    }
}