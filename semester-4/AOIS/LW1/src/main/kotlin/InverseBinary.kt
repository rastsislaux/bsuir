fun Int.toInverseBinary(): String {
    if (this == 0) return "0"

    val binary = CharArray(32)
    var i = 31
    var n = if (this < 0) -this else this

    while (n != 0) {
        binary[i--] = if (this < 0) (49 - (n and 1)).toChar() else ((n and 1) + 48).toChar()
        n = n ushr 1
    }
    while (i > 0) binary[i--] = if (this < 0) '1' else '0'

    binary[0] = if (this < 0) '1' else '0'
    return String(binary.copyOfRange(0, 32))
}

fun addInverseBinaries(x: String, y: String): String {
    if (x[0] == '1' && y[0] == '0') return addInverseBinaries(y, x)

    return when {
        x[0] == '0' && y[0] == '0' -> "0" + addBinaries(x, y)
        x[0] == '0' && y[0] == '1' && x.isModuleGreater(y.inverseBits()) -> "0" + addBinaries("0" + addBinaries(x, y), 1.toInverseBinary())
        x[0] == '0' && y[0] == '1' && y.inverseBits().isModuleGreater(x) -> "1" + addBinaries(x, y)
        x[0] == '1' && y[0] == '1' -> "1" + addBinaries("1" + addBinaries(x, y), 1.toInverseBinary())
        else -> throw RuntimeException("Unreachable")
    }
}

fun subtractInverseBinaries(x: String, y: String): String {
    val pair = addBinariesWithCarry(x, y.inverseBits())

    return when (pair.second) {
        1 -> pair.first[0] + addBinaries("0${pair.first}", 1.toInverseBinary())
        else -> "${pair.first[0]}${pair.first}"
    }
}
