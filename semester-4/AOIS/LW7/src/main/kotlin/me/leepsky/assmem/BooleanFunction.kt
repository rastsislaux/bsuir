package me.leepsky.assmem

enum class BooleanFunction(
    val apply: (Word, Word) -> Boolean
) {
    /**
     * Represents XOR (eXclusive or, strong disjunction) operation. Returns `true` if all
     * corresponding bits return `true` when XORed.
     */
    XOR({ w1, w2 -> w1.operation(w2) { i, bit -> bit xor w2[i] } }),

    /**
     * Represents logical equality operation. Returns `true` if all bits are equal.
     */
    EQ ({ w1, w2 -> w1.operation(w2) { i, bit -> bit ==  w2[i] } }),

    /**
     * Represents OR (weak disjunction) operation. Returns `true` if all corresponding
     * bites return `true` when ORed.
     */
    OR ({ w1, w2 -> w1.operation(w2) { i, bit -> bit or  w2[i] } }),

    /**
     * Represents AND (conjunction) operation. Returns `true` if all corresponding bits
     * return `true` when ANDed.
     */
    AND({ w1, w2 -> w1.operation(w2) { i, bit -> bit and w2[i] } })
}
