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
import static org.junit.jupiter.api.Assertions.fail;
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

    // startup with invalid argument: abc
    @Test
    void startWIArgabc() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"abc"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}  
    }

    // startup with invalid argument: -1
    @Test
    void startWIArgMinusOne() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"-1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}  
    }

    // startup with invalid argument: 3
    @Test
    void startWIArgDrei() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"3"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}  
    }

    // startup with invalid argument: 0
    @Test
    void startWIArgZero() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"0"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}  
    }

    // startup with more than 1 argument: extra
    @Test
    void startWMoreArgExtra() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"1", "extra"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}
    }

    // startup with more than 1 argument: 1 2
    @Test
    void startWMoreArgOneTwo() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));
        App.main(new String[]{"1", "2"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Please, input a valid option [1-2]", reader.readLine());
        } catch(IOException e){}
    }

    // startup with 01
    @Test
    void startWZeroOne() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = "q" + System.lineSeparator();
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"01"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Hello!", reader.readLine());
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

    // Startup Message and Order with Human
    @Test
    void startMessOrdWithHuman() throws IOException{
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

    // Startup Message and Order with Computer
    @Test
    void startMessOrdWithComputer() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = "q" + System.lineSeparator();
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"2"});


        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            assertEquals("Hello!", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("Player#2's turn", reader.readLine());
        } catch(IOException e){}
    }

    // Board render: initialization and update
    @Test
    void boardRender() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"5","q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(1, reader);

            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
        
            assertEquals("Player#1's turn", reader.readLine());

            assertEquals("| 0 | 0 | 0 |", reader.readLine()); 
            assertEquals("| 0 | 1 | 0 |", reader.readLine()); // game updates
            assertEquals("| 0 | 0 | 0 |", reader.readLine());

        } catch(IOException e){}
    }

    // human non-int output three times
    @Test
    void humanNonInt() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"abc","@","","q"});
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

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(13, reader);
            assertEquals("The cell is occupied!", reader.readLine());
            assertEquals("Player#1's turn", reader.readLine());
        } catch(IOException e){}
    }

    // quit game with q
    @Test
    void quitGameWq() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = "q" + System.lineSeparator();
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});


        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(5, reader);
            assertEquals("End of the game", reader.readLine());
        } catch(IOException e){}
    }

    // verify q case sensitivity
    @Test
    void qSensitivity() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"Q"," q","q ","q"});
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

    // Human win detection on row/column/diagonal
    @Test
    void humanWinDetect() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"3","5","7"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(24, reader);
            assertEquals("Player#1 won!", reader.readLine());
        } catch(IOException e){}
    }

    // Computer win detection on row/column/diagonal
    @Test
    void ComputerWinDetect() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"4","5","7"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(28, reader);
            assertEquals("Player#2 won!", reader.readLine());
        } catch(IOException e){}
    }

    // Draw Detection
    @Test
    void DrawDetect() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"2","4","5","7","9"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(40, reader);
            assertEquals("It is a draw!", reader.readLine());
        } catch(IOException e){}
    }

    // Computer behaviour detection
    @Test
    void ComputerBehaviourDetect() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"2","q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"2"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(5, reader);
            assertEquals("| 2 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            skipLines(5, reader);
            assertEquals("| 2 | 1 | 2 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
        } catch(IOException e){}
    }

    // Board integrity after every move. Note: not sure if this satisfies the requirement
    @Test
    void BoardIntegrity() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"2","3","5","q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(5, reader);
            assertEquals("| 0 | 1 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            skipLines(5, reader);
            assertEquals("| 2 | 1 | 1 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            skipLines(5, reader);
            assertEquals("| 2 | 1 | 1 |", reader.readLine());
            assertEquals("| 2 | 1 | 0 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());
            skipLines(1, reader);
            assertEquals("| 2 | 1 | 1 |", reader.readLine());
            assertEquals("| 2 | 1 | 2 |", reader.readLine());
            assertEquals("| 0 | 0 | 0 |", reader.readLine());

        } catch(IOException e){}
    }

    // Turn prompt sequence correctness
    @Test
    void PromptCorrectness() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"abc","2","1","3","q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(5, reader);
            assertEquals("Please, input a valid number [1-9]", reader.readLine());
            
            assertEquals("Player#1's turn", reader.readLine());
            skipLines(3, reader);
            assertEquals("Player#2's turn", reader.readLine());
            skipLines(3, reader);
            assertEquals("Player#1's turn", reader.readLine());
            assertEquals("The cell is occupied!", reader.readLine());
            assertEquals("Player#1's turn", reader.readLine());
            skipLines(3, reader);
            assertEquals("Player#2's turn", reader.readLine());


        } catch(IOException e){}
    }

    // Program termination behavior on final states: Player 1 wins
    @Test
    void PlayerOneWins() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"4","5","6"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(24, reader);
            assertEquals("Player#1 won!", reader.readLine());
            assertEquals(null, reader.readLine());
        } catch(IOException e){}
    }

    // Program termination behavior on final states: Player 2 wins
    @Test
    void PlayerTwoWins() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"4","5","7"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(28, reader);
            assertEquals("Player#2 won!", reader.readLine());
            assertEquals(null, reader.readLine());
        } catch(IOException e){}
    }

    // Program termination behavior on final states: Player 1 entered "q"
    @Test
    void UserEntersq() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"4","5","3","q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(29, reader);
            assertEquals("End of the game", reader.readLine());
            assertEquals(null, reader.readLine());
        } catch(IOException e){}
    }

    // Program termination behavior on final states: draw
    @Test
    void DrawTermination() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"4","5","3","8","9"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(40, reader);
            assertEquals("It is a draw!", reader.readLine());
            assertEquals(null, reader.readLine());
        } catch(IOException e){}
    }

    // Input robustness under rapid invalid retries. Note: dont know how can do it rapidly
    @Test
    void RapidInvalidEntries() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"test","test","test","test","test","test","test","test","test","test","XY",
        "XY", "XY","XY","XY","XY","XY","XY","XY","XY","100","100","100","100"
        ,"100","100","100","100","100","-1","-1","-1","-1","-1","-1", "q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(75, reader);
            assertEquals("End of the game", reader.readLine());
            assertEquals(null, reader.readLine());
        } catch(IOException e){}
    }

    // Output consistency with exact required strings: done above

    // non-integer parsing normalization
    @Test
    void NotTrimmedBeforeValidation() throws IOException{
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        String data = joinByNewline(new String[]{"5 " ,"q"});
        byte[] byteArray = data.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        System.setIn(inputStream);

        App.main(new String[]{"1"});

        byte[] printout = outputByteArray.toByteArray();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8))){
            skipLines(5, reader);
            assertEquals("Please, input a valid number [1-9]", reader.readLine());
        } catch(IOException e){}
    }

    // test the program exits gracefully: stimulate Ctrl Z
    @Test
    void testGracefulExitOnBrokenPipe() {
        try {
        // 2. Set the sabotaged stream
        System.setOut(new BrokenPipeStream());

        // 3. Run the app with "1" (Human starts)
        // We expect the app to try and print "Hello!" or the board and hit the exception
        App.main(new String[]{"1"});
        
        } catch (Exception e) {
            // 4. If your App.main doesn't catch the error, the test fails here.
            // If App.main handles it gracefully, the test passes.
            fail("The application did not handle the broken pipe gracefully!");
        }
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

// turn the Exception when using EOF