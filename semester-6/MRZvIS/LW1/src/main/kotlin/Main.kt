import by.bsuir.Number8
import java.lang.Exception

fun lwMain() {
    print("Multiplicand: ")
    val number1 = Number8(readln())

    print("Factor: ")
    val number2 = Number8(readln())

    print("$number1 + $number2 = ${number1 * number2}}")
}


fun main() {
    try {
        lwMain()
    } catch (e: Exception) {
        println("Ошибка: ${e.message} (${e::class.qualifiedName})")
    }
}
