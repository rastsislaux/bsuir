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

open class ArithmeticPipeline<T>(vararg steps: Step<T>) {

    protected var tick: Int = 0
    protected val steps: List<Step<T>>

    init {
        this.steps = steps.toList()
    }

    fun run(vararg input: T): List<T> {
        val inputList = input.toMutableList()
        val originalSize = inputList.size
        val output = ArrayDeque<T>()

        while (output.size != originalSize) {
            tick(inputList, output)
        }

        return output
    }

    open fun preTick() { }

    open fun postTick() { }

    private fun tick(input: MutableList<T>, output: ArrayDeque<T>) {
        tick++
        for (i in steps.lastIndex downTo 1) {
            steps[i].content = steps[i - 1].content
            steps[i - 1].content = null
        }

        if (input.isNotEmpty()) {
            steps[0].content = input.removeLast()
        }

        preTick()
        for (step in steps) {
            step.doWork()
        }
        postTick()

        if (steps[steps.lastIndex].content != null) {
            output.addFirst(steps[steps.lastIndex].content!!)
            steps[steps.lastIndex].content = null
        }
    }

}

abstract class Step<T> {
    var content: T? = null

    fun doWork() {
        if (content != null) {
            content = doWork(content!!)
        }
    }

    abstract fun doWork(input: T): T
}

data class MultiplicationTriple(
    val multiplicand: Number8,
    val factor: Number8,
    val partialSum: Number8
)

class MultiplicationStep: Step<MultiplicationTriple>() {

    override fun doWork(input: MultiplicationTriple): MultiplicationTriple {
        val newPartialSum = (input.partialSum shl 1) + (if (input.factor[0]) input.multiplicand else Number8.ZERO)
        val newFactor = input.factor shl 1
        return MultiplicationTriple(input.multiplicand, newFactor, newPartialSum)
    }

}
