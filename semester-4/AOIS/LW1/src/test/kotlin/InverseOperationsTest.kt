import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class InverseOperationsTest {

    @Test
    @DisplayName("10 + 21 = 31")
    fun addition_test1() {
        assertEquals(31.toInverseBinary(), addInverseBinaries(plusTen, plusTwentyOne))
    }
    @Test
    @DisplayName("10 + (-21) = -11")
    fun addition_test2() {
        assertEquals((-11).toInverseBinary(), addInverseBinaries(plusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("(-10) + 21 = 11")
    fun addition_test3() {
        assertEquals(11.toInverseBinary(), addInverseBinaries(minusTen, plusTwentyOne))
    }

    @Test
    @DisplayName("(-10) + (-21) = -31")
    fun addition_test4() {
        assertEquals((-31).toInverseBinary(), addInverseBinaries(minusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("21 + 10 = 31")
    fun addition_test5() {
        assertEquals(31.toInverseBinary(), addInverseBinaries(plusTwentyOne, plusTen))
    }
    @Test
    @DisplayName("(-21) + 10 = -11")
    fun addition_test6() {
        assertEquals((-11).toInverseBinary(), addInverseBinaries(minusTwentyOne, plusTen))
    }

    @Test
    @DisplayName("21 + (-10) = 11")
    fun addition_test7() {
        assertEquals(11.toInverseBinary(), addInverseBinaries(plusTwentyOne, minusTen))
    }

    @Test
    @DisplayName("(-21) + (-10) = -31")
    fun addition_test8() {
        assertEquals((-31).toInverseBinary(), addInverseBinaries(minusTwentyOne, minusTen))
    }

    @Test
    @DisplayName("10 - 21 = -11")
    fun subtraction_test1() {
        assertEquals((-11).toInverseBinary(), subtractInverseBinaries(plusTen, plusTwentyOne))
    }
    @Test
    @DisplayName("10 - (-21) = 31")
    fun subtraction_test2() {
        assertEquals(31.toInverseBinary(), subtractInverseBinaries(plusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("(-10) - 21 = -31")
    fun subtraction_test3() {
        assertEquals((-31).toInverseBinary(), subtractInverseBinaries(minusTen, plusTwentyOne))
    }

    @Test
    @DisplayName("(-10) - (-21) = -31")
    fun subtraction_test4() {
        assertEquals(11.toInverseBinary(), subtractInverseBinaries(minusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("21 - 10 = 31")
    fun subtraction_test5() {
        assertEquals(11.toInverseBinary(), subtractInverseBinaries(plusTwentyOne, plusTen))
    }
    @Test
    @DisplayName("(-21) - 10 = -31")
    fun subtraction_test6() {
        assertEquals((-31).toInverseBinary(), subtractInverseBinaries(minusTwentyOne, plusTen))
    }

    @Test
    @DisplayName("21 - (-10) = 31")
    fun subtraction_test7() {
        assertEquals(31.toInverseBinary(), subtractInverseBinaries(plusTwentyOne, minusTen))
    }

    @Test
    @DisplayName("(-21) - (-10) = -11")
    fun subtraction_test8() {
        assertEquals((-11).toInverseBinary(), subtractInverseBinaries(minusTwentyOne, minusTen))
    }

    companion object {
        private val plusTen = 10.toInverseBinary();
        private val plusTwentyOne = 21.toInverseBinary();
        private val minusTen = (-10).toInverseBinary();
        private val minusTwentyOne = (-21).toInverseBinary();
    }

}
