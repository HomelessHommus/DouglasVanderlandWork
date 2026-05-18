import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.ls.LSOutput;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BoatTest {

    Boat boat1;
    Boat boat2;
    static Boat repeatedUseBoat;
    static int crewcount = 0;

    @BeforeAll
    static void staticMethod() {
        repeatedUseBoat = new Boat();
    }
    @BeforeEach
    void setup() {
        Boat.count = 0;
        boat1 = new Boat();
        boat2 = new Boat("MyBoat", "Class 1", 123);

    }

    @DisplayName("Testing both constructors")
    @Tag("Douglas")
    @Tag("Critical")
    @Order(1)
    @Test void testConstructor() {
        assertAll(
                () -> assertEquals("MyBoat", boat2.getName()),
                () -> assertEquals("Class 1", boat2.getbClass()),
                () -> assertEquals(123, boat2.getRegNum()),
                () -> assertEquals(2, boat2.getSeqNum()),
                () -> assertEquals("unknown", boat1.getName()),
                () -> assertEquals("unknown", boat1.getbClass()),
                () -> assertEquals(-1, boat1.getRegNum()),
                () -> assertEquals(1, boat1.getSeqNum())
        );
    }

    @DisplayName("Testing adding multiple crew members")
    @Tag("Douglas")
    @Tag("Core")
    @Order(2)
    @RepeatedTest(5) void repeatedTest() {
        crewcount++;

        repeatedUseBoat.addCrew("crewName" + crewcount);

        assertEquals(crewcount, repeatedUseBoat.getCrew().size());
    }

    @DisplayName("Para test with string array")
    @Tag("Douglas")
    @Tag("Core")
    @Order(3)
    @ParameterizedTest()
    @ValueSource(strings = {"one", "two", "three", "four", "five"})
    public void StringTest(String crewName) {
        boat1.addCrew(crewName);
        boat2.addCrew(crewName);
        assertTrue(boat1.getCrew().contains(crewName));
    }

    @DisplayName("Para test with int array")
    @Tag("Douglas")
    @Tag("Core")
    @Order(4)
    @ParameterizedTest()
    @ValueSource(ints = {1,2,3,4,5})
    public void intTest(int regNum) {
        boat1.setRegNum(regNum);

        assertEquals(regNum, boat1.getRegNum());
    }


    @DisplayName("Para test with csv source")
    @Tag("Douglas")
    @Tag("Additional")
    @Order(5)
    @ParameterizedTest
    @CsvSource({
            "FirstBoat, Class 1, 1",
            "SecondBoat, Class 2, 2",
            "ThirdBoat, Class 3, 3"
    })
    public void testCsvSource(String name, String bClass, int regNum){
        assumeTrue(regNum > 0);

        Boat boat = new Boat(name, bClass, regNum);

        assertAll(
                () -> assertEquals(name, boat.getName()),
                () -> assertEquals(bClass, boat.getbClass()),
                () -> assertEquals(regNum, boat.getRegNum())
        );

        assumingThat(regNum < 2,
                () -> assertEquals("FirstBoat", boat.getName())
                );
    }

    @DisplayName("Para test with csv file source")
    @Tag("Douglas")
    @Tag("Additional")
    @Order(6)
    @ParameterizedTest
    @CsvFileSource(resources = "boats.csv", numLinesToSkip = 1)
    public void testCsvFileSource(String name, String bClass, int regNum) {

        Boat boat = new Boat(name, bClass, regNum);

        assertAll(
                () -> assertEquals(name, boat.getName()),
                () -> assertEquals(bClass, boat.getbClass()),
                () -> assertEquals(regNum, boat.getRegNum())
        );
    }

    @DisplayName("Testing the setters")
    @Tag("Douglas")
    @Tag("Additional")
    @Order(7)
    @Test
    public void testSetters() {
        Boat.count = 0;
        Boat boat = new Boat();
        Boat boat2 = new Boat();
        boat.setName("Boat 1");
        boat.setbClass("Class 1");
        boat.setRegNum(1);
        boat.addCrew("1 crewman");

        assertAll(
                () -> assertEquals("Boat 1", boat.getName()),
                () -> assertEquals("Class 1", boat.getbClass()),
                () -> assertEquals(1, boat.getRegNum()),
                () -> assertEquals("Total Number of Boats made = 2", Boat.printTotal()),
                () -> assertEquals("practical2.Boat Boat 1, " +
                        "Class = Class 1, " +
                        "#Crew = 1, " +
                        "Registration # = KA 1, " +
                        "Sequence # = 1", boat.toString())
        );
    }
}