import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BoatClassComparatorTest {

    @DisplayName("Testing Comparator")
    @Tag("Douglas")
    @Tag("Critical")
    @Test
    public void testComparator() {
        BoatClassComparator bcc = new BoatClassComparator();
        Boat boat = new Boat("Name1", "Class1", 1);
        Boat boat2 = new Boat("Name2", "Class1", 2);

        assertEquals(-1, bcc.compare(boat, boat2));
    }

}