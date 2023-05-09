package me.leepsky.assmem.memory

import me.leepsky.assmem.AMException
import me.leepsky.assmem.BooleanFunction
import me.leepsky.assmem.Word
import me.leepsky.assmem.checkWord
import me.leepsky.assmem.createWord
import kotlin.random.Random

open class SAscm private constructor(
    private val wordSize: Int,
    private val data: Array<Word>
): Ascm(wordSize, data), StringAssociativeMemory {

    /**
     * Find the nearest value from below.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element lesser that given found
     */
    override fun nearestBelow(word: String) = nearestBelow(createWord(word))

    /**
     * Find the nearest value from above.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element greater that given found
     */
    override fun nearestAbove(word: String) = nearestAbove(createWord(word))

    /**
     * Find all values in between some range.
     *
     * @param lower lower border of the search
     * @param higher higher border of the search
     *
     * @throws AMException if some words are invalid
     */
    override fun between(lower: String, higher: String) = between(createWord(lower), createWord(higher))

    /**
     * Find many words that are close to the given pattern.
     *
     * @param pattern a word that is a pattern for the search
     *
     * @throws AMException if some words are invalid
     */
    override fun findMany(pattern: String) = findMany(createWord(pattern))

    /**
     * Find the only word that is the closest to the given pattern.
     *
     * @param pattern a word that is a pattern for the search
     *
     * @throws AMException if some words are invalid
     */
    override fun findOne(pattern: String) = findOne(createWord(pattern))

    /**
     * Find by boolean function.
     *
     * @param pattern a word that is a pattern for the search
     * @param func boolean function that would be applied to words
     *
     * @throws AMException if some words are invalid
     */
    override fun find(pattern: String, func: BooleanFunction) = find(createWord(pattern), func)

    companion object {

        /**
         * Generates a random memory.
         *
         * @param wordCount number of the words in the memory, 16 by default
         * @param wordSize number of bits in one word, 8 by default
         */
        fun random(wordCount: Int = 16, wordSize: Int = 8) =
            SAscm(wordSize, Array(wordCount) { BooleanArray(wordSize) { Random.nextBoolean() } })

        /**
         * Generates a memory from an array of given words.
         *
         * @param words an array of words, must be of the same length
         *
         * @throws AMException when list is empty, or when words are of a different length
         */
        fun words(vararg words: String): SAscm {
            if (words.isEmpty()) {
                throw AMException("Cant infer wordSize from empty words list.")
            }
            val wordSize = words[0].length
            val typedWords = words.map { createWord(it) }
            typedWords.forEach { checkWord(wordSize, it) }

            return SAscm(wordSize, typedWords.toTypedArray())
        }

    }

}
