package vgu.hihi.ttt.basic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    void testApp() {
        assertTrue(true);
    }

    // startup without argument
    @Test
    void startWoArg(){
        // change the system out to another PrintStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // call the main function without arguments
        App.main(new String[]{});

        // check if the sequence is equal
        String expectedMessage = "Please, input a valid option [1-2]" + System.lineSeparator();
        assertTrue(expectedMessage.equals(outContent.toString()));

        // reset the System.out to normal
        System.setOut(System.out);
    }
}
