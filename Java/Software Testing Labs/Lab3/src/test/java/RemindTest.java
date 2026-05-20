import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RemindTest {
    private final InputStream originalInputStream = System.in;
    private final PrintStream originalOutputStream = System.out;
    private final ByteArrayOutputStream captureOutputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        // Reassigns the "standard" output stream to "captureOutputStream".
        System.setOut(new PrintStream(captureOutputStream));
    }

    @AfterEach
    public void tearDown() {
        // Reassigns the "standard" input stream back to what was originally stored in "originalInputStream".
        System.setIn(originalInputStream);
        // Reassigns the "standard" output stream back to what was originally stored in "originalOutputStream".
        System.setOut(originalOutputStream);
    }

    @Test
    @Tag("Douglas")
    @Tag("Core")
    @Order(1)
    @DisplayName("Example test that demonstrates how to test the input and output streams")
    void inputAndOutputSteamExampleTest() {
        String input =
                "m" + System.lineSeparator() +
                        "Example message";
        ByteArrayInputStream captureInputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(captureInputStream);
        Remind.main(null);
        assertEquals(
                "Enter 'm' for Memo or 't' for Todo:" + System.lineSeparator() +
                        "Enter a message:" + System.lineSeparator() +
                        "memo: Example message" + System.lineSeparator(),
                captureOutputStream.toString()
        );
    }

    @Test
    @Tag("Douglas")
    @Tag("Core")
    @Order(2)
    @DisplayName("Testing else statement")
    void inputElseTest() {
        String input =
                "t" + System.lineSeparator() + "20/05/2026" +
                        System.lineSeparator() + "Uni";
        ByteArrayInputStream captureInputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(captureInputStream);
        Remind.main(null);
        assertEquals(
                "Enter 'm' for Memo or 't' for Todo:" + System.lineSeparator() +
                        "Enter when:" + System.lineSeparator() +
                        "Enter what:"  + System.lineSeparator() +
                        "todo: 20/5/26 Uni" + System.lineSeparator(),
                captureOutputStream.toString()
        );
    }

    @Test
    @Tag("Douglas")
    @Tag("Core")
    @Order(2)
    @DisplayName("Testing else statement")
    void testConstructor() {
        Remind remind = new Remind();
        assertNotNull(remind);
    }

}