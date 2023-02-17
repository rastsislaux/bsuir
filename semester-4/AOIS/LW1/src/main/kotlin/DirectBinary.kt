fun Int.toDirectBinary(): String {
    if (this == 0) return "0"

    val binary = CharArray(32)
    var i = 31
    var n = if (this < 0) -this else this

    while (n != 0) {
        binary[i--] = ((n and 1) + 48).toChar()
        n = n ushr 1
    }
    while (i > 0) binary[i--] = '0'

    binary[0] = if (this < 0) '1' else '0'
    return String(binary.copyOfRange(0, 32))
}

fun addDirectBinaries(x: String, y: String) = when {
    x[0] == '0' && y[0] == '0' -> "0" + addBinaries(x, y)
    x[0] == '0' && y[0] == '1' -> (if (y.isModuleGreater(x)) "1" else "0") + subtractBinaryCodes(x, y)
    x[0] == '1' && y[0] == '0' -> (if (y.isModuleGreater(x)) "0" else "1") + subtractBinaryCodes(x, y)
    x[0] == '1' && y[0] == '1' -> "1" + addBinaries(x, y)
    else -> throw RuntimeException("Unreachable")
}

fun subtractDirectBinaries(x: String, y: String) = when {
    x[0] == '0' && y[0] == '0' -> (if (y.isModuleGreater(x)) "1" else "0") + subtractBinaryCodes(x, y)
    x[0] == '0' && y[0] == '1' -> "0" + addBinaries(x, y)
    x[0] == '1' && y[0] == '0' -> "1" + addBinaries(x, y)
    x[0] == '1' && y[0] == '1' -> (if (y.isModuleGreater(x)) "0" else "1") + subtractBinaryCodes(x, y)
    else -> throw RuntimeException("Unreachable")
}

private fun subtractBinaryCodes(x: String, y: String): String {
    if (y.isModuleGreater(x)) return subtractBinaryCodes(y, x)

    var result = ""
    var borrow = 0

    for (i in 31 downTo 1) {
        var difference = x[i].code - y[i].code - borrow
        if (difference < 0) {
            difference += 2
            borrow = 1
        } else borrow = 0
        result = difference.toString() + result
    }

    return result
}
