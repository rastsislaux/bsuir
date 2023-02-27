import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DirectMultiplicationTest {

    @Test
    @DisplayName("10 * 21 = 210")
    fun multiplication_test1() {
        assertEquals(210.db, 10.db * 21.db)
    }
    
    @Test
    @DisplayName("10 * (-21) = -210")
    fun multiplication_test2() {
        assertEquals((-210).db, 10.db * (-21).db)
    }

    @Test
    @DisplayName("(-10) * 21 = -210")
    fun multiplication_test3() {
        assertEquals((-210).db, (-10).db * 21.db)
    }

    @Test
    @DisplayName("(-10) * (-21) = 210")
    fun multiplication_test4() {
        assertEquals(210.db, (-10).db * (-21).db)
    }

    @Test
    @DisplayName("21 * 10 = 210")
    fun multiplication_test5() {
        assertEquals(210.db, 21.db * 10.db)
    }
    @Test
    @DisplayName("(-21) * 10 = -210")
    fun multiplication_test6() {
        assertEquals((-210).db, (-21).db * 10.db)
    }

    @Test
    @DisplayName("21 * (-10) = -210")
    fun multiplication_test7() {
        assertEquals((-210).db, 21.db * (-10).db)
    }

    @Test
    @DisplayName("(-21) * (-10) = 210")
    fun multiplication_test8() {
        assertEquals(210.db, (-21).db * (-10).db)
    }

}
