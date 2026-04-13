package vgu.hihi.ttt.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
class AppTest {
    private final PrintStream originalOut = System.out;
    private PipedOutputStream outputStream;
    private BufferedReader scanner;

    @BeforeEach
   void setUp() { 
       
        outputStream = new PipedOutputStream();
        try {
            PipedInputStream inputStream = new PipedInputStream(outputStream); // Connect in constructor
            scanner = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            // Logger.getLogger(AbstractPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

       System.setOut(new PrintStream(outputStream)); }

    @AfterEach
    void tearDown() { System.setOut(originalOut); }

    /**
     * Rigorous Test :-)
     */
    @Test
    void testApp() {
        assertTrue(true);
    }

// consider adding PipedOutputStream(= PipedInputStream??) and BufferedReader in @BeforeEach
// and set System.out back to normal in @AfterEach

    // startup without argument
    @Test
    void startWoArg() throws IOException{
        App.main(new String[]{});
        assertEquals("Please, input a valid option [1-2]", scanner.readLine());
    }

    // startup with invalid argument
    @Test
    void startWIArg() throws IOException{
        App.main(new String[]{"abc"});
        assertEquals("Please, input a valid option [1-2]", scanner.readLine());
    }

    // startup with more than 1 argument
    @Test
    void startWMoreArg() throws IOException{
        App.main(new String[]{"1", "extra"});
        assertEquals("Please, input a valid option [1-2]", scanner.readLine());
    }
    
    // startup: test with space "1 "
    @Test
    void startWSpacedArg() throws IOException{
        App.main(new String[]{"1 "});
        assertEquals("Please, input a valid option [1-2]", scanner.readLine());
    }

    // startup: test with space "'1'"
    @Test
    void startWQuotedArg() throws IOException{
        App.main(new String[]{"'1'"});
        assertEquals("Please, input a valid option [1-2]", scanner.readLine());
    }
}
