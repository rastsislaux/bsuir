import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DirectAdditionTest {

    @Test
    @DisplayName("10 + 21 = 31")
    fun addition_test1() {
        assertEquals(31.db, 10.db + 21.db)
    }
    @Test
    @DisplayName("10 + (-21) = -11")
    fun addition_test2() {
        assertEquals((-11).db, 10.db + (-21).db)
    }

    @Test
    @DisplayName("(-10) + 21 = 11")
    fun addition_test3() {
        assertEquals(11.db, (-10).db + 21.db)
    }

    @Test
    @DisplayName("(-10) + (-21) = -31")
    fun addition_test4() {
        assertEquals((-31).db, (-10).db + (-21).db)
    }

    @Test
    @DisplayName("21 + 10 = 31")
    fun addition_test5() {
        assertEquals(31.db, 21.db + 10.db)
    }
    @Test
    @DisplayName("(-21) + 10 = -11")
    fun addition_test6() {
        assertEquals((-11).db, (-21).db + 10.db)
    }

    @Test
    @DisplayName("21 + (-10) = 11")
    fun addition_test7() {
        assertEquals(11.db, 21.db + (-10).db)
    }

    @Test
    @DisplayName("(-21) + (-10) = -31")
    fun addition_test8() {
        assertEquals((-31).db, (-21).db + (-10).db)
    }

}
