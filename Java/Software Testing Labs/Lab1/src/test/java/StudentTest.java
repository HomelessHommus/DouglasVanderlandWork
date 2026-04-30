import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    Student Doug;

    @BeforeEach
    void setUp() {
        Doug = new Student(0475, "Vanderland", "Douglas", 2002);
    }

    @DisplayName("Testing the constructor and getters")
    @Test void testConstructorAndGetters() {
        assertAll(
                () -> assertEquals(0475, Doug.getIdNumber()),
                () -> assertEquals("Vanderland", Doug.getFamilyName()),
                () -> assertEquals("Douglas", Doug.getPersonalName()),
                () -> assertEquals(2002, Doug.getYearOfBirth())
        );
    }

    @DisplayName("Test the setter for family name")
    @Test void setFamilyName() {
        Doug.setFamilyName("Something");
        assertEquals("Something", Doug.getFamilyName());
    }



    @DisplayName("Test the setter for personal name")
    @Test void setPersonalName() {
        Doug.setPersonalName("Douglas");
        assertEquals("Douglas", Doug.getPersonalName());
    }


    @DisplayName("Test the setter for YOB")
    @Test void setYearOfBirth() {
        Doug.setYearOfBirth(2000);
        assertEquals(2000, Doug.getYearOfBirth());
    }

    @DisplayName("Test the getter for age")
    @Test void getAge() {
        assertEquals(24, Doug.getAge());
    }



}