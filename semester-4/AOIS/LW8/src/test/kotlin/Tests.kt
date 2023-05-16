import me.leepsky.assmem.BooleanFunction
import me.leepsky.assmem.createWord
import me.leepsky.assmem.memory.StringAssociativeMemory
import me.leepsky.assmem.str
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val memory = StringAssociativeMemory.words(
    "0000111110001100", "0011010111011011", "0010001000110001", "0010010100011111",
    "0001010110110100", "0110011011010011", "1110100010011101", "0101111110100000",
    "0100101111001100", "1001010011010001", "0010011011011101", "1001001001101011",
    "0111110011110001", "1111010011011111", "1010110100000111", "1100011001111111"
)

class Tests {

    @Test
    fun `Чтение слов`() {
        // Чтение слов
        assertTrue { memory[0].contentEquals(createWord("0000111110001100")) }
        assertTrue { memory[1].contentEquals(createWord("0011010111011011")) }
        assertTrue { memory[2].contentEquals(createWord("0010001000110001")) }
    }

    @Test
    fun `Запись слов`() {
        val updated = memory
            .setWord(0, createWord("1111111111111111"))
            .setWord(1, createWord("0000000000000000"))
            .setWord(2, createWord("1010101010101010"))

        assertTrue{ updated[0].contentEquals(createWord("1111111111111111")) }
        assertTrue{ updated[1].contentEquals(createWord("0000000000000000")) }
        assertTrue{ updated[2].contentEquals(createWord("1010101010101010")) }
    }

    @Test
    fun `Чтение столбцов`() {
        assertTrue { memory.getColumn(0).contentEquals(createWord("0000001001010111")) }
        assertTrue { memory.getColumn(1).contentEquals(createWord("0000011110001101")) }
        assertTrue { memory.getColumn(2).contentEquals(createWord("0111011000101110")) }
    }

    @Test
    fun `Запись столбцов`() {
        val updated2 = memory
            .setColumn(0, createWord("1111111111111111"))
            .setColumn(1, createWord("0000000000000000"))
            .setColumn(2, createWord("1010101010101010"))

        assertTrue { updated2.getColumn(0).contentEquals(createWord("1111111111111111")) }
        assertTrue { updated2.getColumn(1).contentEquals(createWord("0000000000000000")) }
        assertTrue { updated2.getColumn(2).contentEquals(createWord("1010101010101010")) }
    }

    @Test
    fun `Логические операции над словами`() {
        assertEquals(
            "0000000000000000",
            memory.performWord(0, "1010101110101011", BooleanFunction.CONSTANT_ZERO).str())

        assertEquals("0010000100011000",
            memory.performWord(1, "0010100100111000", BooleanFunction.AND).str())

        assertEquals("0000000000010001",
            memory.performWord(2, "1110001010101100", BooleanFunction.INHIBITION).str())

        assertEquals("0010010100011111",
            memory.performWord(3, "1010100101010100", BooleanFunction.REPEAT_FIRST).str())

        assertEquals("0110101001000000",
            memory.performWord(4, "0110101101010100", BooleanFunction.REVERSE_INHIBITION).str())

        assertEquals("1010101110101011",
            memory.performWord(5, "1010101110101011", BooleanFunction.REPEAT_SECOND).str())

        assertEquals("0001011011001001",
            memory.performWord(6, "1111111001010100", BooleanFunction.XOR).str())

        assertEquals("1111111111101011",
            memory.performWord(7, "1110100001001011", BooleanFunction.OR).str())

        assertEquals("1010000000100010",
            memory.performWord(8, "0001010101011101", BooleanFunction.NOR).str())

        assertEquals("1101011001110100",
            memory.performWord(9, "1011110101011010", BooleanFunction.EQ).str())

        assertEquals("0100001001010110",
            memory.performWord(10, "1011110110101001", BooleanFunction.NEGATE_SECOND).str())

        assertEquals("1001111101111111",
            memory.performWord(11, "1110001010100001", BooleanFunction.REVERSE_IMPLICATION).str())

        assertEquals("1000001100001110",
            memory.performWord(12, "0001010101011110", BooleanFunction.NEGATE_FIRST).str())

        assertEquals("0001111111101001",
            memory.performWord(13, "0001010111001001", BooleanFunction.IMPLICATION).str())

        assertEquals("0101011111111101",
            memory.performWord(14, "1010100000000010", BooleanFunction.NAND).str())

        assertEquals("1111111111111111",
            memory.performWord(15, "0001011011111001", BooleanFunction.CONSTANT_ONE).str())
    }

    @Test
    fun `Логические операции над столбцами`() {
        assertEquals(
            "0000000000000000",
            memory.performColumn(0, "1010101110101011", BooleanFunction.CONSTANT_ZERO).str())

        assertEquals("0000000100001000",
            memory.performColumn(1, "0010100100111000", BooleanFunction.AND).str())

        assertEquals("0001010000000010",
            memory.performColumn(2, "1110001010101100", BooleanFunction.INHIBITION).str())

        assertEquals("0100100101011100",
            memory.performColumn(3, "1010100101010100", BooleanFunction.REPEAT_FIRST).str())

        assertEquals("0110100001010100",
            memory.performColumn(4, "0110101101010100", BooleanFunction.REVERSE_INHIBITION).str())

        assertEquals("1010101110101011",
            memory.performColumn(5, "1010101110101011", BooleanFunction.REPEAT_SECOND).str())

        assertEquals("0101101111100101",
            memory.performColumn(6, "1111111001010100", BooleanFunction.XOR).str())

        assertEquals("1111100111001011",
            memory.performColumn(7, "1110100001001011", BooleanFunction.OR).str())

        assertEquals("0010000000000010",
            memory.performColumn(8, "0001010101011101", BooleanFunction.NOR).str())

        assertEquals("0000011001011000",
            memory.performColumn(9, "1011110101011010", BooleanFunction.EQ).str())

        assertEquals("0100001001010110",
            memory.performColumn(10, "1011110110101001", BooleanFunction.NEGATE_SECOND).str())

        assertEquals("0111111101111111",
            memory.performColumn(11, "1110001010100001", BooleanFunction.REVERSE_IMPLICATION).str())

        assertEquals("0010110101001010",
            memory.performColumn(12, "0001010101011110", BooleanFunction.NEGATE_FIRST).str())

        assertEquals("0111010111011001",
            memory.performColumn(13, "0001010111001001", BooleanFunction.IMPLICATION).str())

        assertEquals("1111111111111101",
            memory.performColumn(14, "1010100000000010", BooleanFunction.NAND).str())

        assertEquals("1111111111111111",
            memory.performColumn(15, "0001011011111001", BooleanFunction.CONSTANT_ONE).str())
    }

    @Test
    fun `Арифметические операции`() {
        val expected = StringAssociativeMemory.words(
            "0000111110010011", "0011010111011011", "0010001000110001", "0010010100011111",
            "0001010110110111", "0110011011010011", "1110100010011101", "0101111110100000",
            "0100101111001100", "1001010011010001", "0010011011011101", "1001001001101011",
            "0111110011110001", "1111010011011111", "1010110100000111", "1100011001111111"
        )

        val result = memory.sumWhere("000")

        (0..15).forEach {
            assertEquals(expected[it].str(), result[it].str())
        }
    }

}