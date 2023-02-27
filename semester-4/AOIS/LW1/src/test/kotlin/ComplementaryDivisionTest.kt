import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ComplementaryDivisionTest {

    @Test
    @DisplayName("10 / 21 = 0,47")
    fun division_test1() {
        assertEquals(0.cb, (10.cb / 21.cb).first)
        assertEquals("01111", (10.cb / 21.cb).second)
    }
    
    @Test
    @DisplayName("10 / (-21) = -0,47")
    fun division_test2() {
        assertEquals(0.cb, (10.cb / (-21).cb).first)
        assertEquals("01111", (10.cb / (-21).cb).second)
    }

    @Test
    @DisplayName("(-10) / 21 = -0.47")
    fun division_test3() {
        assertEquals(0.cb, ((-10).cb / 21.cb).first)
        assertEquals("01111", ((-10).cb / 21.cb).second)
    }

    @Test
    @DisplayName("(-10) / (-21) = 0.47")
    fun division_test4() {
        assertEquals(0.cb, ((-10).cb / (-21).cb).first)
        assertEquals("01111", ((-10).cb / (-21).cb).second)
    }

    @Test
    @DisplayName("21 / 10 = 2.1")
    fun division_test5() {
        assertEquals(2.cb, (21.cb / 10.cb).first)
        assertEquals("00011", (21.cb / 10.cb).second)
    }
    @Test
    @DisplayName("(-21) / 10 = -2.1")
    fun division_test6() {
        assertEquals((-2).cb, ((-21).cb / 10.cb).first)
        assertEquals("00011", ((-21).cb / 10.cb).second)
    }

    @Test
    @DisplayName("21 / (-10) = -2.1")
    fun division_test7() {
        assertEquals((-2).cb, (21.cb / (-10).cb).first)
        assertEquals("00011", (21.cb / (-10).cb).second)
    }

    @Test
    @DisplayName("(-21) / (-10) = 2.1")
    fun division_test8() {
        assertEquals(2.cb, ((-21).cb / (-10).cb).first)
        assertEquals("00011", ((-21).cb / (-10).cb).second)
    }

}
