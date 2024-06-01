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

import by.bsuir.ArithmeticPipeline
import by.bsuir.BinaryNumber
import by.bsuir.MultiplicationStep
import by.bsuir.MultiplicationTriple
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.awt.Desktop
import java.awt.Dimension
import java.io.File
import java.io.IOException
import java.lang.StringBuilder
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class MultiplicationPipeline: ArithmeticPipeline<MultiplicationTriple>(
    MultiplicationStep(),
    MultiplicationStep(),
    MultiplicationStep(),
    MultiplicationStep(),
    MultiplicationStep(),
    MultiplicationStep(),
    MultiplicationStep(),
    MultiplicationStep(),
) {

    val result: MutableList<List<MultiplicationTriple?>> = mutableListOf()

    override fun preTick() {
        val tickState = steps.map { it.content }
        result.add(tickState)
    }

    fun showHTMLReport() {
        val html = buildHTMLReport()
        openHTML(html)
    }

    private fun buildHTMLReport(): String {
        val sb = StringBuilder("<html><body style='font-size: 18px;'><table>\n<tr>\n")
        sb.append("\t<th style=\"border: 1px solid black;\">Step</th>\n")
        for (i in 0..<result.size) {
            sb.append("\t<th style=\"border: 1px solid black;\">Tick ${i + 1}</th>\n")
        }
        sb.append("</tr>\n")

        for (stepIndex in steps.indices) {
            sb.append("<tr>\n")
            sb.append("\t<td style=\"border: 1px solid black;\">Step ${stepIndex + 1}</td>")
            for (tickIndex in 0..<result.size) {
                val step = result[tickIndex][stepIndex]


                if (step == null) {
                    sb.append("<td nowrap=\"nowrap\" style=\"border: 1px solid black;\"></td>")
                } else {
                    val partialProduct = if (step.factor[0]) step.multiplicand else BinaryNumber(16, 0)
                    val newPartialSum =
                        (step.partialSum.shl(1)).plus((if (step.factor[0]) step.multiplicand else BinaryNumber(16, 0)))
                    sb.append(
                        "\t<td nowrap=\"nowrap\" style=\"border: 1px solid black;\">\n" +
                                "\t\tM: ${step.multiplicand.toBinaryString()} (${step.multiplicand.toInt()}) -> ${step.multiplicand.toBinaryString()} (${step.multiplicand.toInt()}) <br>\n" +
                                "\t\tF: ${step.factor.toBinaryString()} (${step.factor.toInt()}) -> ${
                                    (step.factor.shl(
                                        1
                                    )).toBinaryString()
                                } (${(step.factor.shl(1)).toInt()})<br>\n" +
                                "\t\tPP: ${partialProduct.toBinaryString()} (${partialProduct.toInt()})<br>\n" +
                                "\t\tPS: ${(step.partialSum shl 1).toBinaryString()} (${(step.partialSum shl 1).toInt()}) -> ${newPartialSum?.toBinaryString()} (${newPartialSum?.toInt()})\n" +
                                "\t</td>\n"
                    )
                }
            }
            sb.append("<tr>\n</body></html>")
        }
        return sb.toString()
    }

    private fun openHTML(html: String) {
        SwingUtilities.invokeLater {
            val editorPane = JEditorPane("text/html", html)
            editorPane.isEditable = false

            val scrollPane = JScrollPane(editorPane)
            scrollPane.preferredSize = Dimension(2600, 1500)

            val frame = JFrame("Report")
            frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            frame.contentPane.add(scrollPane)
            frame.pack()
            frame.isVisible = true
        }
    }

}

val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

fun lwMain() {
    val x: List<List<Int>> = GSON.fromJson(File("input.json").readText(), object: TypeToken<List<List<Int>>>() { }.type)

    if ( x.any { it[0] > 255 || it[1] > 255 } ) {
        throw IllegalStateException("Numbers must fit in 8 bits.")
    }

    val multiplicationPipeline = MultiplicationPipeline()

    multiplicationPipeline.run(
        *x.map { MultiplicationTriple(BinaryNumber(16, it[0]), BinaryNumber(8, it[1]), BinaryNumber(16, 0)) }
            .toTypedArray()
    )

    multiplicationPipeline.showHTMLReport()
}

fun main() {
    try {
        lwMain()
    } catch (e: Exception) {
        println("Ошибка: ${e.message} (${e::class.qualifiedName})")
    }
}
