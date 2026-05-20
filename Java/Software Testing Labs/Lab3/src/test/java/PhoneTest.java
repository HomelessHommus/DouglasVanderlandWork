import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PhoneTest {

    @DisplayName("Testing")
    @Tag("Douglas")
    @Tag("Core")
    @Order(1)
    @Test
    void testPhone() {}

}