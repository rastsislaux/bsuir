infix fun IntArray.shl(shift: Int): IntArray {
    val result = IntArray(this.size + shift)
    for (i in this.indices) {
        result[i + shift] = this[i]
    }
    return result
}

class ComplementaryBinary(bits: IntArray): Binary(bits) {

    override val abs: ComplementaryBinary
        get() = ComplementaryBinary(inversed)

    val int: Int
        get() {
            var num = 0
            for (i in 0 until 32) {
                num = num shl 1
                if (bits[i] != 0) {
                    num = num or 1
                }
            }
            return if (bits[0] != 0) -(num.inv() + 1) else num
        }

    override fun set(index: Int, value: Int): ComplementaryBinary {
        return super.set(index, value) as ComplementaryBinary
    }

    operator fun plus(other: ComplementaryBinary): ComplementaryBinary {
        if (bits[0] == 1 && other.bits[0] == 0) return other + this

        return when {
            bits[0] == 0 && other.bits[0] == 0 -> ComplementaryBinary(addRawBinaries(bits, other.bits))
            bits[0] == 0 && other.bits[0] == 1 -> {
                if (gt(other.abs)) ComplementaryBinary(addRawBinaries(bits, other.bits)).set(0, 0)
                else ComplementaryBinary(addRawBinaries(bits, other.bits)).set(0, 1)
            }
            bits[0] == 1 && other.bits[0] == 1 -> ComplementaryBinary(addRawBinaries(bits, other.bits)).set(0, 1)
            else -> throw RuntimeException("Unreachable")
        }
    }

    operator fun times(other: ComplementaryBinary): ComplementaryBinary {
        val sign = if (this[0] xor other[0] == 0) 1 else -1
        val absA = if (this[0] == 1) -this else this
        val absB = if (other[0] == 1) -other else other

        var result = 0.cb
        var counter = 0.cb
        while (counter < absB) {
            result += absA
            counter += 1.cb
        }

        return if (sign == -1) -result
               else result
    }

    operator fun div(other: ComplementaryBinary): Pair<ComplementaryBinary, String> {
        val sign = if (this[0] xor other[0] == 0) 1.cb else (-1).cb
        val absA = if (this[0] == 1) -this else this
        val absB = if (other[0] == 1) -other else other

        var quotient = 0.cb
        var remainder = ComplementaryBinary(absA.bits.copyOf())
        while (remainder >= absB) {
            remainder -= absB
            quotient += 1.cb
        }

        return sign * quotient to mantissa(remainder, absB)
    }

    private fun mantissa(remainder: ComplementaryBinary, absB: ComplementaryBinary): String {
        var rem = remainder
        var mantissa = ""
        for (i in 1..5) {
            rem *= 2.cb
            if (rem >= absB) {
                mantissa += "1"
                rem -= absB
            } else {
                mantissa += "0"
            }
        }
        return mantissa
    }

    operator fun compareTo(other: ComplementaryBinary) = (this - other).int

    operator fun minus(other: ComplementaryBinary) = this + -other

    operator fun unaryMinus() = ComplementaryBinary(inversed) + 1.cb

}

val Int.cb: ComplementaryBinary get() {
    if (this == 0) return ComplementaryBinary(IntArray(32) { 0 })

    val binary = IntArray(32)
    var i = 31
    var n = if (this < 0) -this - 1 else this

    while (n != 0) {
        binary[i--] = if (this < 0) 1 - (n and 1) else n and 1
        n = n ushr 1
    }
    while (i > 0) binary[i--] = if (this < 0) 1 else 0

    binary[0] = if (this < 0) 1 else 0
    return ComplementaryBinary(binary)
}
