package me.leepsky.assmem

/**
 * Word is a number of bits (represented as BooleanArray).
 */
typealias Word = BooleanArray

/**
 * Word can be represented as String.
 *
 * For instance, {True, True, False, False} will result into "1100".
 */
fun Word.str() = joinToString("") { if (it) "1" else "0" }

/**
 * Word can be represented as Int.
 *
 * For instance, {True, True, True} will result into 7.
 */
fun Word.int() = str().toInt(2)

/**
 * Perform a binary operation on two words with result as word.
 *
 * @param word a second word to perform an operation with
 * @param operation a functor that is a definition of operation
 */
fun Word.operation(word: Word, operation: (Int, Boolean) -> Boolean): Word {
    checkWord(size, word);
    return mapIndexed(operation).toBooleanArray()
}

/**
 * Word can be constructed from a String, where 1 represents `true`, and 0 represents `false`.
 *
 * @param size size of a resulting word
 * @param s string to construct a word from
 */
fun createWord(size: Int, s: String): Word = s.map { it == '1' }.toBooleanArray()
    .reversedArray()
    .copyOf(size)
    .reversedArray()

/**
 * Word can be constructed from a String, where 1 represents `true`, and 0 represents `false`.
 * The length of desired word is inferred from the length of a string.
 */
fun createWord(s: String): Word = createWord(s.length, s)

/**
 * Word can be constructed from an Int, using its binary representation.
 *
 * @param size size of a resulting word
 * @param i int to construct a word from
 */
fun createWord(size: Int, i: Int): Word = createWord(size, i.toString(2))

/**
 * Throw exception if the word doesn't have desired size.
 *
 * @param size desired size of a word
 * @param word the word to check size of
 */
fun checkWord(size: Int, word: Word) {
    if (word.size != size) {
        throw AMException("Word has the wrong size!")
    }
}