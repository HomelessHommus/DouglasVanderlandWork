import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemoTest {
    private final PrintStream originalOutputStream = System.out;
    private final ByteArrayOutputStream captureOutputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        // Reassigns the "standard" output stream to "captureOutputStream".
        System.setOut(new PrintStream(captureOutputStream));
    }

    @AfterEach
    public void tearDown() {
        // Reassigns the "standard" output stream back to what was originally stored in "originalOutputStream".
        System.setOut(originalOutputStream);
    }

    @Test
    @Order(1)
    @Tag("Tester")
    @Tag("Core")
    @DisplayName("Example test that demonstrates how to test the output stream")
    void outputSteamExampleTest() {
        Memo memo = new Memo("Example memo");
        memo.print();
        assertEquals(
                "memo: Example memo" + System.lineSeparator(),
                captureOutputStream.toString()
        );
    }


    @DisplayName("Test Null and Empty Inputs")
    @Tag("Douglas")
    @Tag("Critical")
    @ParameterizedTest
    @NullSource
    @EmptySource
    public void nullAndEmptyTest(String input) {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        Memo memo = new Memo(input);
        memo.print();

        String output = in.toString();

        assertEquals("memo: " + String.valueOf(input) + System.lineSeparator(), output);
    }


}

