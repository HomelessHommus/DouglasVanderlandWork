import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PhoneComparatorTest {

    @DisplayName("Test Phone Comparator")
    @Tag("Douglas")
    @Tag("Core")
    @Order(1)
    @Test
    public void testPhoneComparator() {

        Phone phone = new Phone("Doug", "1");
        Phone phone2 = new Phone("Doug", "1");
        Phone phone3 = new Phone("Sam", "3");

        PhoneComparator pc = new PhoneComparator();

        assertAll(
                () -> assertTrue(pc.compare(phone, phone3) != 0),
                () -> assertTrue(pc.compare(phone, phone2) == 0)
        );
    }
}