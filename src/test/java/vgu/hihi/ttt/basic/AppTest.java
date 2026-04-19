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

    private final java.io.InputStream originalIn = System.in;
    private PipedOutputStream testOutToGameIn;

    @BeforeEach
   void setUp() throws IOException { 
       
        outputStream = new PipedOutputStream();
        try {
            PipedInputStream inputStream = new PipedInputStream(outputStream); // Connect in constructor
            scanner = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            // Logger.getLogger(AbstractPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

       System.setOut(new PrintStream(outputStream)); 
        
       // set up system in to stimulate human player input
        testOutToGameIn = new PipedOutputStream();
        PipedInputStream gameReadStream = new PipedInputStream(testOutToGameIn);
        System.setIn(gameReadStream);
    }

    @AfterEach
    void tearDown() throws IOException { 
        System.setOut(originalOut); 
        System.setIn(originalIn);
        outputStream.close();
        testOutToGameIn.close();
    }

    private void simulateTyping(String text) throws IOException {
        testOutToGameIn.write((text + System.lineSeparator()).getBytes());
        testOutToGameIn.flush();
    }

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

    // Startup Message and Order
    @Test
    void startMessOrd() throws IOException{
        App.main(new String[]{"1"});
        assertEquals("Hello!", scanner.readLine());
        assertEquals("| 0 | 0 | 0 |", scanner.readLine());
        assertEquals("| 0 | 0 | 0 |", scanner.readLine());
        assertEquals("| 0 | 0 | 0 |", scanner.readLine());
        assertEquals("Player#1's turn", scanner.readLine());
        
        simulateTyping("q");
    }

    // Human Non-Integer Input
    // Typing 3 next after x meaning that the program waits for output
    // also, since the rep is changed: | 0 | 0 | 1 | -> mapping is correct 
    @Test
    void humanNonInt() throws IOException{
        App.main(new String[]{"1"});
        for(int i = 0; i < 5; i++) scanner.readLine();
        simulateTyping("x");
        simulateTyping("3");
        
        assertEquals("Please, input a valid number [1-9]", scanner.readLine());
        
        assertEquals("Player#1's turn", scanner.readLine());

        assertEquals("| 0 | 0 | 1 |", scanner.readLine()); // game continues

        simulateTyping("q");
    }

    // Human Out-of-Range Integer Input
    @Test
    void humanOutRangeIn() throws IOException{
        App.main(new String[]{"1"});
        for(int i = 0; i < 5; i++) scanner.readLine();
        simulateTyping("0");
        simulateTyping("10");
        simulateTyping("-5");
        
        assertEquals("Please, input a valid number [1-9]", scanner.readLine());
        assertEquals("Player#1's turn", scanner.readLine());

        assertEquals("Please, input a valid number [1-9]", scanner.readLine());
        assertEquals("Player#1's turn", scanner.readLine());

        assertEquals("Please, input a valid number [1-9]", scanner.readLine());
        assertEquals("Player#1's turn", scanner.readLine());

        simulateTyping("q");
    }

    // Human Occupied Cell Input
    @Test
    void humanOccupiedIn() throws IOException{
        App.main(new String[]{"2"});
        simulateTyping("1");

        // skip hello!, ini board, ... and computer turn, Player#1's turn
        for(int i = 0; i < 10; i++) scanner.readLine();

        simulateTyping("1");

        assertEquals("The cell is occupied!", scanner.readLine());
        assertEquals("Player#1's turn", scanner.readLine());

        simulateTyping("q");

    }
}
