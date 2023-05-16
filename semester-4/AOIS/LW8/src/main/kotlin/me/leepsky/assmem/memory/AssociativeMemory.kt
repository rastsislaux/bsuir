package me.leepsky.assmem.memory

import me.leepsky.assmem.AMException
import me.leepsky.assmem.BooleanFunction
import me.leepsky.assmem.Word

/**
 * Associative Memory is a memory, which can be addressed not by addresses or other names,
 * but by its content.
 */
interface AssociativeMemory {

    /**
     * The greatest value that can be represented in this memory.
     */
    val max: Word

    /**
     * The lowest value that can be represented in this memory.
     */
    val min: Word

    /**
     * Number of words in the memory.
     */
    val size: Int

    /**
     * Size of a word.
     */
    val wordSize: Int

    /**
     * Find the nearest value from below.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element lesser that given found
     */
    fun below(word: Word): Word

    /**
     * Find the nearest value from above.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element greater that given found
     */
    fun above(word: Word): Word

    /**
     * Find the greatest value in the memory.
     */
    fun max(): Word

    /**
     * Find the lowest value in the memory.
     */
    fun min(): Word

    /**
     * Find all values in between some range.
     *
     * @param lower lower border of the search
     * @param higher higher border of the search
     *
     * @throws AMException if some words are invalid
     */
    fun between(lower: Word, higher: Word): Ascm

    /**
     * Sort the memory
     *
     * @param comparator a function that compares the values in memory
     */
    fun sort(comparator: (Int) -> Int = { it }): Ascm

    /**
     * Find many words that are close to the given pattern.
     *
     * @param pattern a word that is a pattern for the search
     *
     * @throws AMException if some words are invalid
     */
    fun findAll(pattern: Word): Ascm

    /**
     * Find the only word that is the closest to the given pattern.
     *
     * @param pattern a word that is a pattern for the search
     */
    fun find(pattern: Word): Word

    /**
     * Find by boolean function.
     *
     * @param pattern a word that is a pattern for the search
     * @param func boolean function that would be applied to words
     *
     * @throws AMException if some words are invalid
     */
    fun find(pattern: Word, func: BooleanFunction): Ascm

    // --------- Laboratory Work 8 --------------

    /**
     * Diagonalize associative memory
     */
    fun diagonalize(): AssociativeMemory

    /**
     * Write word.
     *
     * @param i index of a word to write
     * @param word the word to write
     */
    fun setWord(i: Int, word: Word): AssociativeMemory

    /**
     * Get word.
     * @param i word number
     */
    operator fun get(i: Int): Word

    /**
     * Write column
     *
     * @param i index of column to write
     * @param word the word to write
     */
    fun setColumn(i: Int, word: Word): AssociativeMemory

    /**
     * Read column
     *
     * @param i column index
     */
    fun getColumn(i: Int): Word

    /**
     * Perform a logic operation on word.
     *
     * @param wordIndex word number (first operand)
     * @param operand second operand
     * @param operation operation to perform
     */
    fun performWord(wordIndex: Int, operand: Word, operation: BooleanFunction): Word

    /**
     * Perform a logic operation on column
     *
     * @param columnIndex column number (first operand)
     * @param operand second operand
     * @param operation operation to perform
     */
    fun performColumn(columnIndex: Int, operand: Word, operation: BooleanFunction): Word

    /**
     * Add A and B fields for words, where V equals to `param`.
     *
     * @param param number to check equality
     */
    fun sumWhere(param: Word): AssociativeMemory

    companion object {

        /**
         * Generates a random memory.
         *
         * @param wordCount number of the words in the memory, 16 by default
         * @param wordSize number of bits in one word, 8 by default
         */
        fun random(wordCount: Int = 16, wordSize: Int = 8) = Ascm.random(wordCount, wordSize)

    }

}
