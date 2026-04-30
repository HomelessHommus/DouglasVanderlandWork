import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ConcatenationTest {

    @DisplayName("Test the default constructor")
    @Test void defaultConstructor() {
        Concatenation concat = new Concatenation();
        assertNotNull(concat);
    }


    @DisplayName("Test concat with a good list")
    @Test void concat() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals("abc", Concatenation.concat(list));
    }

    @DisplayName("Test concat with null and empty list")
    @ParameterizedTest(name="Run {index} - List: {arguments}")
    @NullAndEmptySource
    public void testConcatNullAndEmpty(ArrayList<String> list) {
        if (list == null) {
            assertThrows(NullPointerException.class, () -> Concatenation.concat(list));
        }
        else {
            assertEquals("", Concatenation.concat(list));
        }
    }

    @DisplayName("Test with one element in a list being a null")
    @Test void testConcatWithNullElement() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        list.add("b");
        assertThrows(NullPointerException.class, () -> Concatenation.concat(list));
    }


}