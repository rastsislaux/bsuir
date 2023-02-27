import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FloatingPointAdditionTest {

    @Test
    fun test1() {
        assertEquals(31f.fp, 10f.fp + 21f.fp)
    }

    @Test
    fun test2() {
        assertEquals(31f.fp, 21f.fp + 10f.fp)
    }

    @Test
    fun params() {
        for (i in 1..20) {
            for (j in 1..20) {
                val opX = i / 2f
                val opY = j / 2f

                assertEquals((opX + opY).fp, opX.fp + opY.fp)
            }
        }
    }

}