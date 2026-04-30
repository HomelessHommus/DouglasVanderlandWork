import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

class MinMaxTest {

    @DisplayName("Test the default constructor")
    @Test void defaultConstructor() {
        MinMax MM = new MinMax();
        assertNotNull(MM);
    }

    @DisplayName("Testing the method with a good list")
    @Test void testMax() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(4, MinMax.sumSL(list));
    }

    @DisplayName("Testing using upper and lower boundaries")
    @Test void testUpperBoundary() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(Integer.MIN_VALUE);
        list.add(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE + Integer.MIN_VALUE, MinMax.sumSL(list));
    }

    @DisplayName("Test MinMax with null and empty list")
    @ParameterizedTest(name="Run {index} - List: {arguments}")
    @NullAndEmptySource
    public void testConcatNullAndEmpty(ArrayList<Integer> list) {
        if (list == null) {
            assertThrows(NullPointerException.class, () -> MinMax.sumSL(list));
        }
        else {
            NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> MinMax.sumSL(list));
        }
    }

    @DisplayName("Test with one element in a list being a null")
    @Test void testConcatWithNullElement() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(null);
        list.add(4);
        assertThrows(NullPointerException.class, () -> MinMax.sumSL(list));
    }


}