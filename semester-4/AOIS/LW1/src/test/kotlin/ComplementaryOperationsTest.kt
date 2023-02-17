import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ComplementaryOperationsTest {

    @Test
    @DisplayName("10 + 21 = 31")
    fun addition_test1() {
        assertEquals(31.toComplementaryBinary(), addComplementaryBinaries(plusTen, plusTwentyOne))
    }
    @Test
    @DisplayName("10 + (-21) = -11")
    fun addition_test2() {
        assertEquals((-11).toComplementaryBinary(), addComplementaryBinaries(plusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("(-10) + 21 = 11")
    fun addition_test3() {
        assertEquals(11.toComplementaryBinary(), addComplementaryBinaries(minusTen, plusTwentyOne))
    }

    @Test
    @DisplayName("(-10) + (-21) = -31")
    fun addition_test4() {
        assertEquals((-31).toComplementaryBinary(), addComplementaryBinaries(minusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("21 + 10 = 31")
    fun addition_test5() {
        assertEquals(31.toComplementaryBinary(), addComplementaryBinaries(plusTwentyOne, plusTen))
    }
    @Test
    @DisplayName("(-21) + 10 = -11")
    fun addition_test6() {
        assertEquals((-11).toComplementaryBinary(), addComplementaryBinaries(minusTwentyOne, plusTen))
    }

    @Test
    @DisplayName("21 + (-10) = 11")
    fun addition_test7() {
        assertEquals(11.toComplementaryBinary(), addComplementaryBinaries(plusTwentyOne, minusTen))
    }

    @Test
    @DisplayName("(-21) + (-10) = -31")
    fun addition_test8() {
        assertEquals((-31).toComplementaryBinary(), addComplementaryBinaries(minusTwentyOne, minusTen))
    }

    @Test
    @DisplayName("10 - 21 = -11")
    fun subtraction_test1() {
        assertEquals((-11).toComplementaryBinary(), subtractComplementaryBinaries(plusTen, plusTwentyOne))
    }
    @Test
    @DisplayName("10 - (-21) = 31")
    fun subtraction_test2() {
        assertEquals(31.toComplementaryBinary(), subtractComplementaryBinaries(plusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("(-10) - 21 = -31")
    fun subtraction_test3() {
        assertEquals((-31).toComplementaryBinary(), subtractComplementaryBinaries(minusTen, plusTwentyOne))
    }

    @Test
    @DisplayName("(-10) - (-21) = -31")
    fun subtraction_test4() {
        assertEquals(11.toComplementaryBinary(), subtractComplementaryBinaries(minusTen, minusTwentyOne))
    }

    @Test
    @DisplayName("21 - 10 = 31")
    fun subtraction_test5() {
        assertEquals(11.toComplementaryBinary(), subtractComplementaryBinaries(plusTwentyOne, plusTen))
    }
    @Test
    @DisplayName("(-21) - 10 = -31")
    fun subtraction_test6() {
        assertEquals((-31).toComplementaryBinary(), subtractComplementaryBinaries(minusTwentyOne, plusTen))
    }

    @Test
    @DisplayName("21 - (-10) = 31")
    fun subtraction_test7() {
        assertEquals(31.toComplementaryBinary(), subtractComplementaryBinaries(plusTwentyOne, minusTen))
    }

    @Test
    @DisplayName("(-21) - (-10) = -11")
    fun subtraction_test8() {
        assertEquals((-11).toComplementaryBinary(), subtractComplementaryBinaries(minusTwentyOne, minusTen))
    }

    companion object {
        private val plusTen = 10.toComplementaryBinary();
        private val plusTwentyOne = 21.toComplementaryBinary();
        private val minusTen = (-10).toComplementaryBinary();
        private val minusTwentyOne = (-21).toComplementaryBinary();
    }

}
