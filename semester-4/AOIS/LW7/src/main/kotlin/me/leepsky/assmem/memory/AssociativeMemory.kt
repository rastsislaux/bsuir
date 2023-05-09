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
     * Find the nearest value from below.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element lesser that given found
     */
    fun nearestBelow(word: Word): Word

    /**
     * Find the nearest value from above.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element greater that given found
     */
    fun nearestAbove(word: Word): Word

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
    fun findMany(pattern: Word): Ascm

    /**
     * Find the only word that is the closest to the given pattern.
     *
     * @param pattern a word that is a pattern for the search
     */
    fun findOne(pattern: Word): Word

    /**
     * Find by boolean function.
     *
     * @param pattern a word that is a pattern for the search
     * @param func boolean function that would be applied to words
     *
     * @throws AMException if some words are invalid
     */
    fun find(pattern: Word, func: BooleanFunction): Ascm

    /**
     * Logical variable g.
     *
     * @param j word number
     * @param i digit number
     */
    fun g(j: Int, i: Int)

    /**
     * Logical variable l.
     *
     * @param j word number
     * @param i digit number
     */
    fun l(j: Int, i: Int)

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
