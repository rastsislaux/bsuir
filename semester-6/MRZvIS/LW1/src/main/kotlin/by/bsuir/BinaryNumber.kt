/*
Лабораторная работа #1 по предмету "Методы решения задач в интеллектуальных системах"

Авторы:
    студенты гр. 121701
    Липский Р. В., Стронгин А. В., Жолнерчик И. А.

Задание:
    (16) реализовать алгоритм вычисления произведения пары 8-разрядных чисел умножением со старших разрядов
         со сдвигом частичной суммы влево

Источники:
    (1) Интеграционная платформа
    (2) Качинский, М. В. Арифметические основы электронных вычислительных средств : учебно-метод. пособие
        / М. В. Качинский, В. Б. Клюс, А. Б. Давыдов. - Минск : БГУИР, 2014. - 64 с. : ил.
 */

package by.bsuir

class BinaryNumber(val bits: Int, private val number: List<Boolean>) {

    constructor(number: String) : this(number.length, strToList(number))

    constructor(bits: Int, number: Int): this(bits, intToList(bits, number))

    infix fun shl(places: Int): BinaryNumber {
        var copy = number
        repeat(places) {
            copy = copy.drop(1) + false
        }
        return BinaryNumber(this.bits, copy)
    }

    operator fun plus(other: BinaryNumber): BinaryNumber {
        val result = mutableListOf<Boolean>()
        var carry = false

        for ((bitA, bitB) in this.number.reversed().zip(other.number.reversed())) {
            val sumBit = bitA xor bitB xor carry
            carry = (bitA && bitB) || (bitA && carry) || (bitB && carry)
            result.add(sumBit)
        }

        return BinaryNumber(result.size, result.reversed())
    }

    operator fun times(other: BinaryNumber): BinaryNumber {
        var partialSum = BinaryNumber(this.bits + other.bits, 0)
        for (i in 0 ..< this.bits) {
            partialSum = (partialSum shl 1) + (if (other[i]) this else BinaryNumber(this.bits + other.bits, 0))
        }
        return partialSum
    }

    operator fun get(other: Int): Boolean {
        return this.number[other]
    }

    override fun toString(): String {
        return  "Number$bits[${toBinaryString()} (${toInt()})]"
    }

    fun toInt(): Int {
        val bs = number.map { if (it) '1' else '0' }.joinToString("")
        return Integer.parseInt(bs, 2)
    }

    fun toBinaryString(): String {
        return number.map { if (it) '1' else '0' }.joinToString("")
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is BinaryNumber) return false

        return this.number == other.number
    }

    override fun hashCode(): Int {
        return this.number.hashCode()
    }

    companion object {

        fun strToList(number: String): List<Boolean> {
            if (number.any { it !in listOf('1', '0') }) {
                throw IllegalArgumentException("Входные числа должны быть двоичными.")
            }

            return number.map { it == '1' }
        }

        fun intToList(bits: Int, value: Int): List<Boolean> {
            val x = Integer.toBinaryString(value)

            if (x.length > bits) {
                val substr = x.substring(x.length - bits, x.length)
                return strToList(substr)
            }

            var str = x
            repeat(bits - x.length) {
                str = "0$str"
            }
            return strToList(str)
        }

    }

}
