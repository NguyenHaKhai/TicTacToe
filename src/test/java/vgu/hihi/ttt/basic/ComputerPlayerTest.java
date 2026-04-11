package vgu.hihi.ttt.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Board object
 */
class ComputerPlayerTest{

    // test makeMove() method: bot plays 1 at an empty Board
    @Test
    void makeMoveETrue(){
        ComputerPlayer bot = new ComputerPlayer(2);
        Board1D testBoard = new Board1D();
        
        bot.makeMove(testBoard);

        assertEquals(testBoard.getCell(0), 2);
    }

    // test makeMove() method: bot plays 1 at an custom Board
    @Test
    void makeMoveCTrue(){
        ComputerPlayer bot = new ComputerPlayer(2);
        Board1D testBoard = new Board1D(3, 3, new int[]{1,1,0,2,1,2,1,1,0});
        
        bot.makeMove(testBoard);

        assertEquals(testBoard.getCell(2), 2);
    }
}