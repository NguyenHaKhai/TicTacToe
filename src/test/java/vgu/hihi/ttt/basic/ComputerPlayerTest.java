package vgu.hihi.ttt.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Board object
 */
class ComputerPlayerTest{

    // test makeMove() method: bot plays 1 at an empty Board: return valid move
    @Test
    void makeMoveETrue(){
        ComputerPlayer bot = new ComputerPlayer(2);
        Board testBoard = new Board1D();
        
        int result = bot.makeMove(testBoard);

        assertEquals(result, 0);
    }

    // test makeMove() method: bot plays 1 at an custom Board: return invalid move
    @Test
    void makeMoveCTrue(){
        ComputerPlayer bot = new ComputerPlayer(2);
        Board testBoard = new Board1D(3, 3, new int[]{1,1,0,2,1,2,1,1,0});
        
        int result = bot.makeMove(testBoard);

        assertEquals(result, -1);
    }
}