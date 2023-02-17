import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DirectOperationsTest {

    @Test
    @DisplayName("10 + 21 = 31")
    fun addition_test1() {
        assertEquals(31.toDirectBinary(), addDirectBinaries(plusTen, plusTwentyOne))
    }
    @Test
    @DisplayName("10 + (-21) = -11")
    fun addition_test2() {
        assertEquals((-11).toDirectBinary(), addDirectBinaries(plusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("(-10) + 21 = 11")
    fun addition_test3() {
        assertEquals(11.toDirectBinary(), addDirectBinaries(minusTen, plusTwentyOne))
    }

    @Test
    @DisplayName("(-10) + (-21) = -31")
    fun addition_test4() {
        assertEquals((-31).toDirectBinary(), addDirectBinaries(minusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("21 + 10 = 31")
    fun addition_test5() {
        assertEquals(31.toDirectBinary(), addDirectBinaries(plusTwentyOne, plusTen))
    }
    @Test
    @DisplayName("(-21) + 10 = -11")
    fun addition_test6() {
        assertEquals((-11).toDirectBinary(), addDirectBinaries(minusTwentyOne, plusTen))
    }

    @Test
    @DisplayName("21 + (-10) = 11")
    fun addition_test7() {
        assertEquals(11.toDirectBinary(), addDirectBinaries(plusTwentyOne, minusTen))
    }

    @Test
    @DisplayName("(-21) + (-10) = -31")
    fun addition_test8() {
        assertEquals((-31).toDirectBinary(), addDirectBinaries(minusTwentyOne, minusTen))
    }

    @Test
    @DisplayName("10 - 21 = -11")
    fun subtraction_test1() {
        assertEquals((-11).toDirectBinary(), subtractDirectBinaries(plusTen, plusTwentyOne))
    }
    @Test
    @DisplayName("10 - (-21) = 31")
    fun subtraction_test2() {
        assertEquals(31.toDirectBinary(), subtractDirectBinaries(plusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("(-10) - 21 = -31")
    fun subtraction_test3() {
        assertEquals((-31).toDirectBinary(), subtractDirectBinaries(minusTen, plusTwentyOne))
    }

    @Test
    @DisplayName("(-10) - (-21) = -31")
    fun subtraction_test4() {
        assertEquals(11.toDirectBinary(), subtractDirectBinaries(minusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("21 - 10 = 31")
    fun subtraction_test5() {
        assertEquals(11.toDirectBinary(), subtractDirectBinaries(plusTwentyOne, plusTen))
    }
    @Test
    @DisplayName("(-21) - 10 = -31")
    fun subtraction_test6() {
        assertEquals((-31).toDirectBinary(), subtractDirectBinaries(minusTwentyOne, plusTen))
    }

    @Test
    @DisplayName("21 - (-10) = 31")
    fun subtraction_test7() {
        assertEquals(31.toDirectBinary(), subtractDirectBinaries(plusTwentyOne, minusTen))
    }

    @Test
    @DisplayName("(-21) - (-10) = -11")
    fun subtraction_test8() {
        assertEquals((-11).toDirectBinary(), subtractDirectBinaries(minusTwentyOne, minusTen))
    }

    companion object {
        private val plusTen = 10.toDirectBinary();
        private val plusTwentyOne = 21.toDirectBinary();
        private val minusTen = (-10).toDirectBinary();
        private val minusTwentyOne = (-21).toDirectBinary();
    }

}
