import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MaximumTest {

    @DisplayName("Test the default constructor")
    @Test void defaultConstructor() {
        Maximum max = new Maximum();
        assertNotNull(max);
    }

    @DisplayName("Test on the first int being the max")
    @Test void testFirstNumberLargest1() {
        assertEquals(9,Maximum.max3(9,3,2));
    }
    @DisplayName("Test on the second int being the max")
    @Test void testFirstNumberLargest2() {
        assertEquals(9,Maximum.max3(3,9,2));
    }
    @DisplayName("Test on the third int being the max")
    @Test void testFirstNumberLargest3() {
        assertEquals(9,Maximum.max3(2,3,9));
    }

    @DisplayName("Testing the maximum value for first int")
    @Test void testFirstNumberLargest4() {
        assertEquals(Integer.MAX_VALUE,Maximum.max3(Integer.MAX_VALUE,Integer.MIN_VALUE,4));
    }
    @DisplayName("Testing the maximum value for second int")
    @Test void testFirstNumberLargest5() {
        assertEquals(Integer.MAX_VALUE,Maximum.max3(Integer.MIN_VALUE,Integer.MAX_VALUE,4));
    }
    @DisplayName("Testing the maximum value for third int")
    @Test void testFirstNumberLargest6() {
        assertEquals(Integer.MAX_VALUE,Maximum.max3(Integer.MIN_VALUE,4,Integer.MAX_VALUE));
    }

    @DisplayName("Testing the minimum value for first int")
    @Test void testFirstNumberLargest7() {
        assertEquals(4,Maximum.max3(Integer.MIN_VALUE,3,4));
    }
    @DisplayName("Testing the minimum value for second int")
    @Test void testFirstNumberLargest8() {
        assertEquals(4,Maximum.max3(2,Integer.MIN_VALUE,4));
    }
    @DisplayName("Testing the minimum value for third int")
    @Test void testFirstNumberLargest9() {
        assertEquals(4,Maximum.max3(3,4,Integer.MIN_VALUE));
    }
}