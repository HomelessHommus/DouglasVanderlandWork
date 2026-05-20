import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TodoTest {

    @DisplayName("Test Date Parse")
    @Tag("Douglas")
    @Tag("Core")
    @Order(1)
    @Test
    void testDataParse() {
        Date date = Todo.dateParse("2026/05/20");
        Date date2 = Todo.dateParse("NA");
        assertAll(
                () -> assertNotNull(date),
                () -> assertNull(date2)
        );
    }

    @DisplayName("Test Date Parse")
    @Tag("Douglas")
    @Tag("Core")
    @Order(2)
    @Test
    void testMatchDate() {
        Todo todo = new Todo(Todo.dateParse("2026/05/20)"), "uni");
        assertAll(
                () -> assertTrue(todo.matchDate(Todo.dateParse("2026/05/20)"))),
                () -> assertFalse(todo.matchDate(Todo.dateParse("2020/05/20)")))
        );
    }

    @DisplayName("Test Date Parse")
    @Tag("Douglas")
    @Tag("Core")
    @Order(3)
    @Test
    void testGetWhen() {
        Date date = Date.from(LocalDate.of(2027, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        Todo todo = new Todo(date, "uni");

        assertEquals(date, todo.getWhen());
    }

}