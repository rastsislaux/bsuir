import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ComplementaryMultiplicationTest {

    @Test
    @DisplayName("10 * 21 = 210")
    fun multiplication_test1() {
        assertEquals(210.cb, 10.cb * 21.cb)
    }
    
    @Test
    @DisplayName("10 * (-21) = -210")
    fun multiplication_test2() {
        assertEquals((-210).cb, 10.cb * (-21).cb)
    }

    @Test
    @DisplayName("(-10) * 21 = -210")
    fun multiplication_test3() {
        assertEquals((-210).cb, (-10).cb * 21.cb)
    }

    @Test
    @DisplayName("(-10) * (-21) = 210")
    fun multiplication_test4() {
        assertEquals(210.cb, (-10).cb * (-21).cb)
    }

    @Test
    @DisplayName("21 * 10 = 210")
    fun multiplication_test5() {
        assertEquals(210.cb, 21.cb * 10.cb)
    }
    @Test
    @DisplayName("(-21) * 10 = -210")
    fun multiplication_test6() {
        assertEquals((-210).cb, (-21).cb * 10.cb)
    }

    @Test
    @DisplayName("21 * (-10) = -210")
    fun multiplication_test7() {
        assertEquals((-210).cb, 21.cb * (-10).cb)
    }

    @Test
    @DisplayName("(-21) * (-10) = 210")
    fun multiplication_test8() {
        assertEquals(210.cb, (-21).cb * (-10).cb)
    }

}
