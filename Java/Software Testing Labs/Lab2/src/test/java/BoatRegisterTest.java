import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class BoatRegisterTest {

    static int boatCount = 0;
    static BoatRegister repeatedUseBoatRegister;

    @BeforeAll
    static void staticMethod() {
        repeatedUseBoatRegister = new BoatRegister();
    }

    @DisplayName("Testing getBoat")
    @Tag("Douglas")
    @Tag("Core")
    @Order(1)
    @Test
    public void testGetBoat() {
        BoatRegister r = new BoatRegister();
        Boat boat = new Boat("Name", "Class", 1);
        r.addBoat(boat);

        assertEquals(boat , r.getBoat(1));
    }

    @DisplayName("Testing getBoatNull")
    @Tag("Douglas")
    @Tag("Core")
    @Order(9)
    @Test
    public void testGetBoatNull() {
        BoatRegister r = new BoatRegister();
        Boat boat = new Boat("Name", "Class", 1);
        r.addBoat(boat);

        assertNull(r.getBoat(2));
    }

    @DisplayName("Testing adding multiple boats")
    @Tag("Douglas")
    @Tag("Critical")
    @Order(2)
    @RepeatedTest(5)
    public void repeatedAddBoats() {
        boatCount++;

        Boat boat = new Boat();

        repeatedUseBoatRegister.addBoat(boat);

        assertEquals(boatCount, repeatedUseBoatRegister.getRegister().size());
    }

    @DisplayName("Testing groupByClass")
    @Tag("Douglas")
    @Tag("Additional")
    @Order(3)
    @Test
    public void testGroupByClass() {
        Boat a = new Boat("First", "1", 10);
        Boat b = new Boat("Second", "2", 20);
        Boat c = new Boat("Third", "3", 30);

        BoatRegister r = new BoatRegister();
        r.addBoat(c);
        r.addBoat(b);
        r.addBoat(a);

        r.groupByClass();

        assertAll(
                () -> assertEquals("1", r.getRegister().get(0).getbClass()),
                () -> assertEquals("2", r.getRegister().get(1).getbClass()),
                () -> assertEquals("3", r.getRegister().get(2).getbClass())
        );
    }

    @DisplayName("Testing getByCondition")
    @Tag("Douglas")
    @Tag("Core")
    @Order(4)
    @Test
    public void testGetByCondition() {

        Boat a = new Boat("First", "1", 10);
        Boat b = new Boat("Second", "1", 20);
        Boat c = new Boat("Third", "2", 30);

        BoatRegister r = new BoatRegister();
        r.addBoat(c);
        r.addBoat(b);
        r.addBoat(a);

        Collection<Boat> toTest = r.getByCondition(boat -> boat.getbClass().equals("1"));

        assertAll(
                () -> assertEquals(2, toTest.size()),
                () -> assertTrue(toTest.contains(a)),
                () -> assertTrue(toTest.contains(b)),
                () -> assertFalse(toTest.contains(c))
        );
    }

    @DisplayName("Testing getBoatsWhoseNameContains")
    @Tag("Douglas")
    @Tag("Additional")
    @Order(5)
    @Test
    public void testGetBoatsWhoseNameContains() {
        Boat a = new Boat("First", "1", 10);
        Boat b = new Boat("Second", "1", 20);
        Boat c = new Boat("Third", "2", 30);

        BoatRegister r = new BoatRegister();
        r.addBoat(c);
        r.addBoat(b);
        r.addBoat(a);

        Collection<Boat> toTest = new ArrayList<>();
        toTest.add(c);
        toTest.add(a);

        assertAll(
                () -> assertEquals(toTest, r.getBoatsWhoseNameContains("i"))
        );
    }

    @DisplayName("Testing deleteRoad")
    @Tag("Douglas")
    @Tag("Critical")
    @Order(6)
    @Test
    public void testDeleteRoad() {
        Boat a = new Boat("First", "1", 10);
        Boat b = new Boat("Second", "1", 20);
        Boat c = new Boat("Third", "2", 30);

        BoatRegister r = new BoatRegister();
        r.addBoat(c);
        r.addBoat(b);
        r.addBoat(a);

        assertAll(
                () -> assertEquals(a, r.deleteBoat(10))
        );
    }

    @DisplayName("Testing toString")
    @Tag("Douglas")
    @Tag("Additional")
    @Order(7)
    @Test
    public void testToString() {
        Boat a = new Boat("First", "1", 10);

        BoatRegister r = new BoatRegister();

        r.addBoat(a);

        assertEquals("[practical2.Boat First, Class = 1, #Crew = 0, Registration # = KA 10, Sequence # = " + a.getSeqNum() + "]", r.toString());
    }

    @DisplayName("Testing defaultConstructor")
    @Tag("Douglas")
    @Tag("Additional")
    @Order(8)
    @Test
    public void testDefaultConstructor() {
        BoatRegister r = new BoatRegister();
        assertNotNull(r);
    }
}