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
import by.bsuir.BinaryNumber
import by.bsuir.MultiplicationStep
import by.bsuir.MultiplicationTriple
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

val multiplicationPipeline = object: ArithmeticPipeline<MultiplicationTriple>(
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
    }

}

val GSON = GsonBuilder().setPrettyPrinting().create()

fun lwMain() {
    val x: List<List<Int>> = GSON.fromJson(File("input.json").readText(), object: TypeToken<List<List<Int>>>() { }.type)

    if ( x.any { it[0] > 255 || it[1] > 255 } ) {
        throw IllegalStateException("Numbers must fit in 8 bits.")
    }

    val res = multiplicationPipeline.run(
        *x.map { MultiplicationTriple(BinaryNumber(16, it[0]), BinaryNumber(16, it[1]) shl 8, BinaryNumber(16, 0)) }
            .toTypedArray()
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
