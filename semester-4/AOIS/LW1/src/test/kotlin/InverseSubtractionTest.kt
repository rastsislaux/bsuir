import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class InverseSubtractionTest {

    @Test
    @DisplayName("10 - 21 = -11")
    fun subtraction_test1() {
        assertEquals((-11).ib, 10.ib - 21.ib)
    }
    @Test
    @DisplayName("10 - (-21) = 31")
    fun subtraction_test2() {
        assertEquals(31.ib, 10.ib - (-21).ib)
    }

    @Test
    @DisplayName("(-10) - 21 = -31")
    fun subtraction_test3() {
        assertEquals((-31).ib, (-10).ib - 21.ib)
    }

    @Test
    @DisplayName("(-10) - (-21) = -31")
    fun subtraction_test4() {
        assertEquals(11.ib, (-10).ib - (-21).ib)
    }

    @Test
    @DisplayName("21 - 10 = 31")
    fun subtraction_test5() {
        assertEquals(11.ib, 21.ib - 10.ib)
    }
    @Test
    @DisplayName("(-21) - 10 = -31")
    fun subtraction_test6() {
        assertEquals((-31).ib, (-21).ib - 10.ib)
    }

    @Test
    @DisplayName("21 - (-10) = 31")
    fun subtraction_test7() {
        assertEquals(31.ib, 21.ib - (-10).ib)
    }

    @Test
    @DisplayName("(-21) - (-10) = -11")
    fun subtraction_test8() {
        assertEquals((-11).ib, (-21).ib - (-10).ib)
    }

}
