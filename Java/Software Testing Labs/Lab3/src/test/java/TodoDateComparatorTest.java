import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TodoDateComparatorTest {

    @DisplayName("Test ToDo Comparator")
    @Tag("Douglas")
    @Tag("Core")
    @Order(1)
    @Test
    public void testPhoneComparator() {
        Date date1 = Date.from(LocalDate.of(2020, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        Date date2 = Date.from(LocalDate.of(2027, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        Todo todo1 = new Todo(date1, "1");
        Todo todo2 = new Todo(date1, "2");
        Todo todo3 = new Todo(date2, "3");

        TodoDateComparator pc = new TodoDateComparator();

        assertAll(
                () -> assertTrue(pc.compare(todo1, todo2) == 0),
                () -> assertTrue(pc.compare(todo1, todo3) != 0)
        );
    }

}