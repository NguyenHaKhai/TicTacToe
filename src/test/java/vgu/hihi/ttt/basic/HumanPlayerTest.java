package vgu.hihi.ttt.basic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Board object
 */
class HumanPlayerTest{

    // test makeMove() method: user plays 3 and that cell equals to 1
    @Test
    void makeMoveTrue(){
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        HumanPlayer Khai = new HumanPlayer(1, in);
        Board testBoard = new Board();
        
        Khai.makeMove(testBoard);

        assertEquals(testBoard.getCell(3 - 1), 1);
    }

    // test makeMove() method: user plays 2 but not allowed(that cell still equals to 2, not 1)
    // here I must add also 3: since the method requires another move until it's valid
    @Test
    void makeMoveInvalidTrue(){
        String input = "2\n3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        HumanPlayer Khai = new HumanPlayer(1, in);
        Board testBoard = new Board(3, 3, new int[]{1,2,0,2,1,2,1,1,0});

        Khai.makeMove(testBoard);

        assertAll("Product Properties",
        () -> assertEquals(testBoard.getCell(2 - 1), 2),
        () -> assertEquals(testBoard.getCell(3 - 1), 1)
    );
    }
}