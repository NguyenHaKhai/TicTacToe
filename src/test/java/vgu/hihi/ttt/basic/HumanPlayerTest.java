package vgu.hihi.ttt.basic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Board object
 */
class HumanPlayerTest{

    @Test
    void makeMoveTrue(){
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());

        HumanPlayer Khai = new HumanPlayer(1, in);
        Board testBoard = new Board();
        
        Khai.makeMove(testBoard);

        assertEquals(testBoard.getCell(3 - 1), 1);

    }
    
    
}