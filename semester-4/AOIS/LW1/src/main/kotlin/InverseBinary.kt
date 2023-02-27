class InverseBinary(bits: IntArray): Binary(bits) {

    override val abs: InverseBinary
        get() = if (bits[0] == 0) this else InverseBinary(this.inversed)

    val int: Int
        get() {
            var num = 0
            for (i in 0 until 32) {
                num = num shl 1
                if (bits[i] != 0) {
                    num = num or 1
                }
            }
            return if (bits[0] != 0) -(num.inv()) else num
        }

    override fun set(index: Int, value: Int) = super.set(index, value) as InverseBinary

    operator fun plus(other: InverseBinary): InverseBinary {
        if (bits[0] == 1 && other.bits[0] == 0) return other + this;

        return when {
            bits[0] == 0 && other.bits[0] == 0 -> InverseBinary(addRawBinaries(bits, other.bits))
            bits[0] == 0 && other.bits[0] == 1 -> {
                if (gt(other.abs)) InverseBinary(addRawBinaries(bits, other.bits)).set(0, 0) + 1.ib
                else InverseBinary(addRawBinaries(bits, other.bits)).set(0, 1)
            }
            bits[0] == 1 && other.bits[0] == 1 -> InverseBinary(addRawBinaries(bits, other.bits)) + 1.ib
            else -> throw RuntimeException("Unreachable")
        }
    }

    operator fun times(other: InverseBinary): InverseBinary {
        val sign = if (this[0] xor other[0] == 0) 1 else -1
        val absA = if (this[0] == 1) -this else this
        val absB = if (other[0] == 1) -other else other

        var result = 0.ib
        var counter = 0.ib
        while (counter < absB) {
            println(result.int)
            result += absA
            counter += 1.ib
        }

        return if (sign == -1) -result
        else result
    }

    operator fun compareTo(other: InverseBinary) = (this - other).int

    operator fun minus(other: InverseBinary) = this + (-other)

    operator fun unaryMinus() = InverseBinary(inversed)

}

val Int.ib: InverseBinary get() {
    if (this == 0) return InverseBinary(IntArray(32) { 0 })

    val binary = IntArray(32)
    var i = 31
    var n = if (this < 0) -this else this

    while (n != 0) {
        binary[i--] = if (this < 0) 1 - (n and 1) else n and 1
        n = n ushr 1
    }
    while (i > 0) binary[i--] = if (this < 0) 1 else 0

    binary[0] = if (this < 0) 1 else 0
    return InverseBinary(binary)
}