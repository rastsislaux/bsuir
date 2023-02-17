fun Int.toComplementaryBinary(): String {
    if (this == 0) return "0"

    val binary = CharArray(32)
    var i = 31
    var n = if (this < 0) -this - 1 else this

    while (n != 0) {
        binary[i--] = if (this < 0) (49 - (n and 1)).toChar() else ((n and 1) + 48).toChar()
        n = n ushr 1
    }
    while (i > 0) binary[i--] = if (this < 0) '1' else '0'

    binary[0] = if (this < 0) '1' else '0'
    return String(binary.copyOfRange(0, 32))
}

fun addComplementaryBinaries(x: String, y: String): String {
    if (x[0] == '1' && y[0] == '0') return addComplementaryBinaries(y, x)

    return when {
        x[0] == '0' && y[0] == '0' -> "0" + addBinaries(x, y)
        x[0] == '0' && y[0] == '1' && x.isModuleGreater(y.inverseBits()) -> "0" + addBinaries(x, y)
        x[0] == '0' && y[0] == '1' && y.inverseBits().isModuleGreater(x) -> "1" + addBinaries(x, y)
        x[0] == '1' && y[0] == '1' -> "1" + addBinaries(x, y)
        else -> throw RuntimeException("Unreachable")
    }
}

fun subtractComplementaryBinaries(x: String, y: String): String {
    val result = addBinaries(x, y.inverseBits())
    return "${result[0]}" + addBinaries("0$result", 1.toComplementaryBinary())
}


