package vgu.hihi.ttt.basic;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    // private PipedOutputStream outputStream;
    // private BufferedReader scanner;

    private final InputStream originalIn = System.in;
    // private PipedOutputStream testOutToGameIn;

    @BeforeEach
   void setUp() throws IOException { 
       
    //     outputStream = new PipedOutputStream();
    //     try {
    //         PipedInputStream inputStream = new PipedInputStream(outputStream); // Connect in constructor
    //         scanner = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    //     } catch (IOException ex) {
    //         // Logger.getLogger(AbstractPlayer.class.getName()).log(Level.SEVERE, null, ex);
    //     }

    //    System.setOut(new PrintStream(outputStream)); 
        
    //    // set up system in to stimulate human player input
    //     testOutToGameIn = new PipedOutputStream();
    //     PipedInputStream gameReadStream = new PipedInputStream(testOutToGameIn);
    //     System.setIn(gameReadStream);
    }

    @AfterEach
    void tearDown() throws IOException { 
        System.setOut(originalOut); 
        System.setIn(originalIn);
        // outputStream.close();
        // testOutToGameIn.close();
    }

    // private void simulateTyping(String text) throws IOException {
    //     testOutToGameIn.write((text + System.lineSeparator()).getBytes());
    //     testOutToGameIn.flush();
    // }

    private void skipLines(int numLine, BufferedReader reader) throws IOException{
        for(int i = 0; i < numLine; i++) reader.readLine();
    }

    private String joinByNewline(String[] arr) {
        return String.join(System.lineSeparator(), arr);
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
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}  
    }

    // startup with invalid argument
    @Test
    void startWIArg() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"abc"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}  
    }

    // startup with more than 1 argument
    @Test
    void startWMoreArg() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"1", "extra"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}
    }
    
    // startup: test with space "1 "
    @Test
    void startWSpacedArg() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"1 "});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}
    }

    // startup: test with space "'1'"
    @Test
    void startWQuotedArg() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"'1'"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}
    }

    // Startup Message and Order
    @Test
    void startMessOrd() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = "q" + System.lineSeparator();
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});


        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Hello!", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("Player#1's turn", reader.readLine());
        } catch(IOException e){}
    }

    // Human Non-Integer Input
    // Typing 3 next after x meaning that the program waits for output
    // also, since the rep is changed: | 0 | 0 | 1 | -> mapping is correct 
    @Test
    void humanNonInt() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"x","3","q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(5, reader);
            assertEquals("Please, input a valid number [1-9]", reader.readLine());
        
            assertEquals("Player#1's turn", reader.readLine());

            assertEquals("| 0 | 0 | 1 |", reader.readLine()); // game continues
        } catch(IOException e){}
    }

    // Human Out-of-Range Integer Input
    @Test
    void humanOutRangeIn() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"0","10","-5","q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(5, reader);
            assertEquals("Please, input a valid number [1-9]", reader.readLine());
            assertEquals("Player#1's turn", reader.readLine());

            assertEquals("Please, input a valid number [1-9]", reader.readLine());
            assertEquals("Player#1's turn", reader.readLine());

            assertEquals("Please, input a valid number [1-9]", reader.readLine());
            assertEquals("Player#1's turn", reader.readLine());
        } catch(IOException e){}
    }

    // Human Occupied Cell Input
    @Test
    void humanOccupiedIn() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"1","1","q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"2"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(9, reader);
            assertEquals("The cell is occupied!", reader.readLine());
            assertEquals("Player#1's turn", reader.readLine());
        } catch(IOException e){}
    }

}
// preparation to remove threads
// ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
// System.setOut(new PrintStream(outputByteArray, true));

// String data = "q" + System.lineSeparator();
// byte[] byteArray = data.getBytes();
// InputStream inputStream = new ByteArrayInputStream(byteArray);
// System.setIn(inputStream);

// App.main(new String[]{})

// byte[] printout = outputByteArray.toByteArray();
// try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){

// }