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

// интерактивность, 16 битный результат
// 8 этапов, посчитать тики правильно

import by.bsuir.ArithmeticPipeline
import by.bsuir.MultiplicationStep
import by.bsuir.MultiplicationTriple
import by.bsuir.Number8

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
    ) {

        override fun postTick() {
            println("===================== TICK $tick =====================")
            steps.forEachIndexed { i, step ->
                println("$i. Multiplicand: ${step.content?.multiplicand} | Factor: ${step.content?.factor} | Partial sum: ${step.content?.partialSum}")
            }

            readln()
            ConsoleUtilsFactory.getInstance().clearConsole()
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

fun main() {
    try {
        lwMain()
    } catch (e: Exception) {
        println("Ошибка: ${e.message} (${e::class.qualifiedName})")
    }
}
