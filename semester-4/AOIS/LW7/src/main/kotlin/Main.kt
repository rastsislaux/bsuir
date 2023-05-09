import me.leepsky.assmem.BooleanFunction
import me.leepsky.assmem.memory.StringAssociativeMemory

fun main() {
    val memory = StringAssociativeMemory.words("00000000", "00001111", "11111111")

    val eq  = memory.find("00000000", BooleanFunction.EQ )
    val xor = memory.find("11110000", BooleanFunction.XOR)
    val and = memory.find("11111111", BooleanFunction.AND)
    val or  = memory.find("11111100", BooleanFunction.OR )

    println("Results:\n eq: $eq,\nxor: $xor,\nand: $and,\n or: $or")
}
