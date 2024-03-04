import by.bsuir.ArithmeticPipeline
import by.bsuir.MultiplicationStep
import by.bsuir.MultiplicationTriple
import by.bsuir.Number8
import java.time.Instant

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