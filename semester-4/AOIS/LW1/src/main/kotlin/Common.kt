fun String.isModuleGreater(other: String): Boolean {
    for (i in 1..31) {
        if (this[i] > other[i]) return true
        if (other[i] > this[i]) return false
    }
    return false
}

fun String.inverseBits(): String {
    val array = this.toCharArray()
    for (i in array.indices) array[i] = if (array[i] == '1') '0' else '1'
    return array.concatToString()
}

fun addBinariesWithCarry(x: String, y: String): Pair<String, Int> {
    var result = ""
    var carry = 0

    for (i in 31 downTo 1) {
        val sum = x[i].code + y[i].code + carry - 96
        carry = if (sum < 2) 0 else 1
        result = if (sum % 2 == 1) "1$result" else "0$result"
    }

    return result to carry
}

fun addBinaries(x: String, y: String) = addBinariesWithCarry(x, y).first
