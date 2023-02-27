class DirectBinary(bits: IntArray): Binary(bits) {

    override fun set(index: Int, value: Int) = super.set(index, value) as DirectBinary

    val int: Int
        get() {
            var num = 0
            for (i in 1 until 32) {
                num = num shl 1
                if (this[i] != 0) {
                    num = num or 1
                }
            }
            return if (this[0] != 0) -num else num
        }

    override val abs: DirectBinary
        get() = set(0, 0)

    operator fun plus(other: DirectBinary): DirectBinary {
        return when {
            bits[0] == 0 && other.bits[0] == 0 -> DirectBinary(addRawBinaries(bits,  other.bits)).set(0, 0)
            bits[0] == 0 && other.bits[0] == 1 -> {
                if (abs.gt(other.abs)) DirectBinary(subRawBinaries(abs.bits, other.abs.bits)).set(0, 0)
                else DirectBinary(subRawBinaries(other.abs.bits, abs.bits)).set(0, 1)
            }
            bits[0] == 1 && other.bits[0] == 0 -> {
                if (abs.gt(other.abs)) DirectBinary(subRawBinaries(abs.bits, other.abs.bits)).set(0, 1)
                else DirectBinary(subRawBinaries(other.abs.bits, abs.bits)).set(0, 0)
            }
            bits[0] == 1 && other.bits[0] == 1 -> DirectBinary(addRawBinaries(abs.bits,  other.abs.bits)).set(0, 1)
            else -> throw RuntimeException("Unreachable")
        }
    }

    operator fun times(other: DirectBinary): DirectBinary {
        val sign = if (this[0] xor other[0] == 0) 1 else -1
        val absA = if (this[0] == 1) -this else this
        val absB = if (other[0] == 1) -other else other

        var result = 0.db
        var counter = 0.db
        while (counter < absB) {
            result += absA
            counter += 1.db
        }

        return if (sign == -1) -result
        else result
    }

    operator fun compareTo(other: DirectBinary) = (this - other).int

    operator fun minus(other: DirectBinary): DirectBinary {
        return this + (-other)
    }

    operator fun unaryMinus(): DirectBinary = set(0, if (bits[0] == 0) 1 else 0)

}

val Int.db: DirectBinary get() {
    if (this == 0) return DirectBinary(IntArray(32) { 0 })

    val binary = IntArray(32)
    var i = 31
    var n = if (this < 0) -this else this

    while (n != 0) {
        binary[i--] = n and 1
        n = n ushr 1
    }
    while (i > 0) binary[i--] = 0

    binary[0] = if (this < 0) 1 else 0
    return DirectBinary(binary)
}
