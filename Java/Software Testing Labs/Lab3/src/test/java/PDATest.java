import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PDATest {

    @DisplayName("Test List Memo")
    @Tag("Douglas")
    @Tag("Core")
    @Order(1)
    @Test
    public void testListMemo() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        Date date = Date.from(LocalDate.of(2026, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date, "Do uni");

        pda.listMemo();

        String output = in.toString();

        assertEquals("memo: Hello World" + System.lineSeparator(), output);
    }

    @DisplayName("Test List Phone")
    @Tag("Douglas")
    @Tag("Core")
    @Order(2)
    @Test
    public void testListPhone() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        Date date = Date.from(LocalDate.of(2026, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date, "Do uni");

        pda.listPhone();

        String output = in.toString();

        assertEquals("phone: Doug 0447800102" + System.lineSeparator(), output);
    }

    @DisplayName("Test List Phone 2")
    @Tag("Douglas")
    @Tag("Core")
    @Order(3)
    @Test
    public void testListPhone2() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        pda.createPhone("Sam", "phonenumber");
        Date date = Date.from(LocalDate.of(2026, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date, "Do uni");

        pda.listPhone("Doug");

        String output = in.toString();

        assertEquals("phone: Doug 0447800102" + System.lineSeparator(), output);
    }

    @DisplayName("Test List Phone 3")
    @Tag("Douglas")
    @Tag("Core")
    @Order(4)
    @Test
    public void testListPhone3() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        pda.createPhone("Sam", "phonenumber");
        Date date = Date.from(LocalDate.of(2026, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date, "Do uni");

        pda.listPhone("Walter");

        String output = in.toString();

        assertEquals("", output);
    }

    @DisplayName("Test List Phone 4")
    @Tag("Douglas")
    @Tag("Core")
    @Order(5)
    @Test
    public void testListPhone4() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        pda.createPhone("Sam", "phonenumber");
        pda.createPhone("Alba", "phonenumber1");
        Date date = Date.from(LocalDate.of(2026, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date, "Do uni");

        pda.listPhoneAlphabetically();

        String output = in.toString();

        assertEquals("phone: Alba phonenumber1" + System.lineSeparator() +
                "phone: Doug 0447800102" + System.lineSeparator() +
                "phone: Sam phonenumber" + System.lineSeparator(), output);
    }

    @DisplayName("Test List To Do")
    @Tag("Douglas")
    @Tag("Core")
    @Order(6)
    @Test
    public void testListToDo() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        Date date = Date.from(LocalDate.of(2026, 5, 20)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());
        pda.createTodo(date, "Do uni");

        pda.listTodo();

        String output = in.toString();

        assertEquals("todo: 20/5/26 Do uni" + System.lineSeparator(), output);
    }

    @DisplayName("Test List To Do 2")
    @Tag("Douglas")
    @Tag("Core")
    @Order(7)
    @Test
    public void testListToDo2() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        Date date = Date.from(LocalDate.of(2026, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date, "Do uni");
        Date date2 = Date.from(LocalDate.of(2027, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date2, "Do uni");

        pda.listTodo(date2);

        String output = in.toString();

        assertEquals("todo: 20/5/27 Do uni" + System.lineSeparator(), output);
    }

    @DisplayName("Test List To Do 3")
    @Tag("Douglas")
    @Tag("Core")
    @Order(8)
    @Test
    public void testListToDo3() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        Date date = Date.from(LocalDate.of(2026, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date, "Do uni");
        Date date2 = Date.from(LocalDate.of(2027, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date2, "Do uni");

        pda.listTodoByDate();

        String output = in.toString();

        assertEquals("todo: 20/5/26 Do uni" + System.lineSeparator() +
                "todo: 20/5/27 Do uni" + System.lineSeparator(), output);
    }

    @DisplayName("Test List To Do 4")
    @Tag("Douglas")
    @Tag("Core")
    @Order(9)
    @Test
    public void testListToDo4() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        pda.createTodo(null, "Do uni");
        Date date2 = Date.from(LocalDate.of(2027, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date2, "Do uni");

        pda.listTodo();

        String output = in.toString();

        assertEquals("todo: 20/5/27 Do uni" + System.lineSeparator(), output);
    }

    @DisplayName("Test List")
    @Tag("Douglas")
    @Tag("Core")
    @Order(10)
    @Test
    public void testList() {

        ByteArrayOutputStream in = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(in));

        PDA pda = new PDA();
        pda.createMemo("Hello World");
        pda.createPhone("Doug", "0447800102");
        Date date = Date.from(LocalDate.of(2026, 5, 20)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        pda.createTodo(date, "Do uni");

        pda.list();

        String output = in.toString();

        assertEquals("memo: Hello World" + System.lineSeparator() +
                "phone: Doug 0447800102" + System.lineSeparator() +
                "todo: 20/5/26 Do uni" + System.lineSeparator(), output);
    }
}