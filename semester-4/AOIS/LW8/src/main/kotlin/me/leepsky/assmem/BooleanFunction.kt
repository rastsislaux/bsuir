package me.leepsky.assmem

enum class BooleanFunction(
    val apply: (Word, Word) -> Word
) {
    /**
     * Represents Constant Zero (F0) operation := 0
     */
    CONSTANT_ZERO({ w1, w2 -> w1.operation(w2) { _, _ -> false } }),

    /**
     * Represents AND (F1, conjunction) operation := x1 & x2
     */
    AND({ w1, w2 -> w1.operation(w2) { i, bit -> bit and w2[i] } }),

    /**
     * Represents Inhibition (F2) operation := x1 & !x2
     */
    INHIBITION({ w1, w2 -> w1.operation(w2) { i, bit -> bit and !w2[i] } }),

    /**
     * Represents repeat first (F3) operation := x1
     */
    REPEAT_FIRST({ w1, w2 -> w1.operation(w2) { _, bit -> bit } }),

    /**
     * Represents reverse inhibition (F4) operation = !x1 & x2
     */
    REVERSE_INHIBITION({ w1, w2 -> w1.operation(w2) { i, bit -> !bit and w2[i] } }),

    /**
     * Represents repeat second (F5) operation = x2
     */
    REPEAT_SECOND({ w1, w2 -> w1.operation(w2) { i, _ -> w2[i] } }),

    /**
     * Represents XOR (F6, eXclusive or, exclusive disjunction) operation := x1 xor x2
     */
    XOR({ w1, w2 -> w1.operation(w2) { i, bit -> bit xor w2[i] } }),

    /**
     * Represents OR (F7, disjunction, weak disjunction) operation := x1 + x2
     */
    OR({ w1, w2 -> w1.operation(w2) { i, bit -> bit or  w2[i] } }),

    /**
     * Represents Pierce's Arrow (F8, NOR, joint denial) operation := !(x1 + x2)
     */
    NOR({ w1, w2 ->  w1.operation(w2) { i, bit -> !(bit or w2[i]) } }),

    /**
     * Represents logical equality (F9) operation := x1 = x2
     */
    EQ ({ w1, w2 -> w1.operation(w2) { i, bit -> bit ==  w2[i] } }),

    /**
     * Represents negate second (F10) operation := !x2
     */
    NEGATE_SECOND({ w1, w2 -> w1.operation(w2) { i, _ -> !w2[i] } }),

    /**
     * Represents reverse implication (F11) operation := x1 or !x2
     */
    REVERSE_IMPLICATION({ w1, w2 -> w1.operation(w2) { i, bit -> bit or !w2[i] } }),

    /**
     * Represents negate first (F12) operation := !x1
     */
    NEGATE_FIRST({ w1, w2 -> w1.operation(w2) { _, bit -> !bit } }),

    /**
     * Represents implication (F13) operation := !x1 + x2
     */
    IMPLICATION({ w1, w2 -> w1.operation(w2) { i, bit -> !bit or w2[i] } }),

    /**
     * Represents Sheffer Stroke (F14, NAND, not and) operation := !(x1 & x2)
     */
    NAND({ w1, w2 -> w1.operation(w2) { i, bit -> !(bit and w2[i])  } }),

    /**
     * Represents Constant One (F15) operation := 1
     */
    CONSTANT_ONE({ w1, w2 -> w1.operation(w2) { _, _ -> true } })
}
