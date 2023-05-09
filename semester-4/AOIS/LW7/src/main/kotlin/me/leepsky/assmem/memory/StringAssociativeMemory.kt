package me.leepsky.assmem.memory

import me.leepsky.assmem.AMException
import me.leepsky.assmem.BooleanFunction
import me.leepsky.assmem.Word

/**
 * Associative Memory is a memory, which can be addressed not by addresses or other names,
 * but by its content.
 *
 * String Associative Memory is a sub interface, that allows using strings as parameters
 * to all methods of Associative Memory.
 */
interface StringAssociativeMemory: AssociativeMemory {

    /**
     * Find the nearest value from below.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element lesser that given found
     */
    fun nearestBelow(word: String): Word

    /**
     * Find the nearest value from above.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element greater that given found
     */
    fun nearestAbove(word: String): Word

    /**
     * Find all values in between some range.
     *
     * @param lower lower border of the search
     * @param higher higher border of the search
     *
     * @throws AMException if some words are invalid
     */
    fun between(lower: String, higher: String): Ascm

    /**
     * Find many words that are close to the given pattern.
     *
     * @param pattern a word that is a pattern for the search
     *
     * @throws AMException if some words are invalid
     */
    fun findMany(pattern: String): Ascm

    /**
     * Find the only word that is the closest to the given pattern.
     *
     * @param pattern a word that is a pattern for the search
     *
     * @throws AMException if some words are invalid
     */
    fun findOne(pattern: String): Word

    /**
     * Find by boolean function.
     *
     * @param pattern a word that is a pattern for the search
     * @param func boolean function that would be applied to words
     *
     * @throws AMException if some words are invalid
     */
    fun find(pattern: String, func: BooleanFunction): Ascm

    companion object {

        /**
         * Generates a random memory.
         *
         * @param wordCount number of the words in the memory, 16 by default
         * @param wordSize number of bits in one word, 8 by default
         */
        fun random(wordCount: Int = 16, wordSize: Int = 8): StringAssociativeMemory = SAscm.random(wordCount, wordSize)

        /**
         * Generates a memory from an array of given words.
         *
         * @param words an array of words, must be of the same length
         *
         * @throws AMException when list is empty, or when words are of a different length
         */
        fun words(vararg words: String): StringAssociativeMemory = SAscm.words(*words)

    }

}
