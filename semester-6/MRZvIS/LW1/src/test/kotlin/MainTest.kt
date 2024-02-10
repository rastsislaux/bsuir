import by.bsuir.Number8
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class Number8Test: StringSpec({

    "test multiplication" {
        checkAll(
            Arb.int(1, 15),
            Arb.int(1, 15)
        ) { num1, num2 ->
            println("Testing with num1=$num1, num2=$num2")
            (Number8(num1) * Number8(num2)) shouldBe Number8(num2 * num1)
        }
    }

})
