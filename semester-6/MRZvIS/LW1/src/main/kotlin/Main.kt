/*
Лабораторная работа #1 по предмету "Методы решения задач в интеллектуальных системах"

Авторы:
    студенты гр. 121701
    Липский Р. В., Жолнерчик И. А., Стронгин А. В.

Задание:
    (16) реализовать алгоритм вычисления произведения пары 8-разрядных чисел умножением со старших разрядов
         со сдвигом частичной суммы влево

Источники:
    (1) Интеграционная платформа
    (2) Качинский, М. В. Арифметические основы электронных вычислительных средств : учебно-метод. пособие
        / М. В. Качинский, В. Б. Клюс, А. Б. Давыдов. - Минск : БГУИР, 2014. - 64 с. : ил.
 */

import by.bsuir.*
import java.lang.Exception
import java.time.Duration
import java.time.Instant
import java.util.stream.IntStream
import kotlin.streams.toList

fun lwMain() {
    val pipeline = object: ArithmeticPipeline<MultiplicationTriple>(
        MultiplicationStep(),
        MultiplicationStep(),
        MultiplicationStep(),
        MultiplicationStep(),
        MultiplicationStep(),
        MultiplicationStep(),
        MultiplicationStep(),
        MultiplicationStep(),
        MultiplicationStep(),
    ) {

        override fun postTick() {
            println("===================== TICK $tick =====================")
            steps.forEachIndexed { i, step ->
                println("$i. Multiplicand: ${step.content?.multiplicand} | Factor: ${step.content?.factor} | Partial sum: ${step.content?.partialSum}")
            }
        }

    }

    val res = pipeline.run(
        MultiplicationTriple(Number8(2), Number8(8),   Number8.ZERO),
        MultiplicationTriple(Number8(6), Number8(6),   Number8.ZERO),
        MultiplicationTriple(Number8(12), Number8(9),  Number8.ZERO),
        MultiplicationTriple(Number8(15), Number8(15), Number8.ZERO),
        MultiplicationTriple(Number8(14), Number8(7),  Number8.ZERO),
        MultiplicationTriple(Number8(3), Number8(7),   Number8.ZERO)
    )

    res.forEach {
        println(it)
    }
}

class Benchmark(private val ops: Int) {

    fun calculateSimple() {
        for (i in 1..ops) {
            val x = Number8(15) * Number8(15)
        }
    }

    fun calculateParallel(list: List<MultiplicationTriple>) {
        ArithmeticPipeline(
            MultiplicationStep(),
            MultiplicationStep(),
            MultiplicationStep(),
            MultiplicationStep(),
            MultiplicationStep(),
            MultiplicationStep(),
            MultiplicationStep(),
            MultiplicationStep(),
            MultiplicationStep(),
        ).run(*list.toTypedArray())
    }

    fun time(x: () -> Unit): Long {
        val start = Instant.now().epochSecond
        x()
        val finish = Instant.now().epochSecond
        return finish - start
    }

    fun run() {
        val timeSimple = time { calculateSimple() }

        val list = mutableListOf<MultiplicationTriple>()
        for (i in 1..ops) {
            list.add(MultiplicationTriple(Number8(15), Number8(15), Number8.ZERO))
        }
        val timeParallel = time { calculateParallel(list) }

        println("Time Simple: ${timeSimple}s")
        println("Time Parallel: ${timeParallel}s")
    }

}

fun main() {
    try {
        Benchmark(5000000).run()
    } catch (e: Exception) {
        println("Ошибка: ${e.message} (${e::class.qualifiedName})")
    }
}
