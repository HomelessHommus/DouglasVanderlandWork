import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SuperBoatClassConditionTest {

    @DisplayName("Testing Satisfies")
    @Tag("Douglas")
    @Tag("Critical")
    @Test
    public void testSatisfies() {
        SuperBoatClassCondition sbcc = new SuperBoatClassCondition();
        Boat a = new Boat("Name1", "Class1", 1);
        Boat b = new Boat("Name2", "Class2", 2);

        assertTrue(sbcc.satisfies(a));
    }

}