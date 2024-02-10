package by.bsuir

class Number8(private val number: List<Boolean>) {

    constructor(number: String) : this(strToList(number))

    constructor(number: Int): this(intToList(number))

    private infix fun shl(places: Int): Number8 {
        var copy = number
        repeat(places) {
            copy = copy.drop(1) + false
        }
        return Number8(copy)
    }

    operator fun plus(other: Number8): Number8 {
        val result = mutableListOf<Boolean>()
        var carry = false

        for ((bitA, bitB) in this.number.reversed().zip(other.number.reversed())) {
            val sumBit = bitA xor bitB xor carry
            carry = (bitA && bitB) || (bitA && carry) || (bitB && carry)
            result.add(sumBit)
        }

        return Number8(result.reversed())
    }

    operator fun times(other: Number8): Number8 {
        var partialSum = ZERO
        for (i in 0..7) {
            partialSum = (partialSum shl 1) + (if (other[i]) this else ZERO)
        }
        return partialSum
    }

    operator fun get(other: Int): Boolean {
        return this.number[other]
    }

    override fun toString(): String {
        val bs = number.map { if (it) '1' else '0' }.joinToString("")
        return  "Number8[$bs (${Integer.parseInt(bs, 2)})]"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is Number8) return false

        return this.number == other.number
    }

    override fun hashCode(): Int {
        return this.number.hashCode()
    }

    companion object {

        fun strToList(number: String): List<Boolean> {
            if (number.length != 8) {
                throw IllegalArgumentException("Входные числа должны иметь 8 разрядов.")
            }

            if (number.any { it !in listOf('1', '0') }) {
                throw IllegalArgumentException("Входные числа должны быть двоичными.")
            }

            return number.map { it == '1' }
        }

        fun intToList(value: Int): List<Boolean> {
            val x = Integer.toBinaryString(value)

            if (x.length > 8) {
                val substr = x.substring(x.length - 8, x.length)
                return strToList(substr)
            }

            var str = x
            repeat(8 - x.length) {
                str = "0$str"
            }
            return strToList(str)
        }

        val ZERO = Number8(0)

    }

}
