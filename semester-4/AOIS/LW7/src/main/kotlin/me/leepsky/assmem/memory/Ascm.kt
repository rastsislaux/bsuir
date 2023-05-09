package me.leepsky.assmem.memory

import me.leepsky.assmem.AMException
import me.leepsky.assmem.BooleanFunction
import me.leepsky.assmem.Word
import me.leepsky.assmem.checkWord
import me.leepsky.assmem.createWord
import me.leepsky.assmem.int
import me.leepsky.assmem.str
import kotlin.random.Random

open class Ascm protected constructor(
    private val wordSize: Int,
    private val data: Array<Word>
): AssociativeMemory {

    /**
     * The greatest value that can be represented in this memory.
     */
    override val max = createWord("1".repeat(wordSize))

    /**
     * The lowest value that can be represented in this memory.
     */
    override val min = createWord("0".repeat(wordSize))

    /**
     * Find the nearest value from below.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element lesser that given found
     */
    override fun nearestBelow(word: Word): Word {
        checkWords(word)
        val value = word.int()

        return data
            .map { it.int() }
            .filter { it < value }
            .max()
            .let { createWord(wordSize, it) }
    }

    /**
     * Find the nearest value from above.
     *
     * @param word search border
     *
     * @throws AMException if some words are invalid
     * @throws NoSuchElementException if no element greater that given found
     */
    override fun nearestAbove(word: Word): Word {
        checkWords(word)
        val value = word.int()

        return data
            .map { it.int() }
            .filter { it > value }
            .min()
            .let { createWord(wordSize, it) }
    }

    /**
     * Find the greatest value in the memory.
     */
    override fun max() = nearestBelow(max)

    /**
     * Find the lowest value in the memory.
     */
    override fun min() = nearestAbove(min)

    /**
     * Find all values in between some range.
     *
     * @param lower lower border of the search
     * @param higher higher border of the search
     *
     * @throws AMException if some words are invalid
     */
    override fun between(lower: Word, higher: Word): Ascm {
        checkWords(lower, higher)

        val low = lower.int()
        val high = higher.int()

        val newData = data.map { it.int() }
            .filter { it in low+1 until high }
            .map { createWord(wordSize, it) }
            .toTypedArray()
        return Ascm(wordSize, newData)
    }

    /**
     * Sort the memory
     *
     * @param comparator a function that compares the values in memory
     */
    override fun sort(comparator: (Int) -> Int): Ascm {
        val sortedData = data
            .map { it.int() }
            .sortedBy(comparator)
            .map { createWord(wordSize, it) }
            .toTypedArray()
        return Ascm(wordSize, sortedData)
    }

    /**
     * Find many words that are close to the given pattern.
     *
     * @param pattern a word that is a pattern for the search
     *
     * @throws AMException if some words are invalid
     */
    override fun findMany(pattern: Word): Ascm {
        checkWords(pattern)

        val sorted = data
            .map { it.mapIndexed { i, bit -> bit == pattern[i] }.count { it } to it }
            .sortedByDescending { it.first }
        val max = sorted[0].first
        return Ascm(wordSize, sorted.filter { it.first == max }.map { it.second }.toTypedArray())
    }

    /**
     * Find the only word that is the closest to the given pattern.
     *
     * @param pattern a word that is a pattern for the search
     */
    override fun findOne(pattern: Word): Word {
        checkWords(pattern)
        return findMany(pattern).max()
    }

    /**
     * Logical variable g.
     *
     * @param j word number
     * @param i digit number
     */
    override fun g(j: Int, i: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Logical variable l.
     *
     * @param j word number
     * @param i digit number
     */
    override fun l(j: Int, i: Int) {
        TODO("Not yet implemented")
    }

    /**
     * Find by boolean function.
     *
     * @param pattern a word that is a pattern for the search
     * @param func boolean function that would be applied to words
     *
     * @throws AMException if some words are invalid
     */
    override fun find(pattern: Word, func: BooleanFunction): Ascm {
        checkWords(pattern)
        return Ascm(wordSize, data.filter { func.apply(it, pattern) }.toTypedArray())
    }

    /**
     * Check a number of words for validity.
     *
     * @param words a number of words to check
     *
     * @throws AMException if some words are invalid
     */
    private fun checkWords(vararg words: Word) {
        words.forEach { checkWord(wordSize, it) }
    }

    override fun toString() = data.joinToString(", ") { it.str() }

    companion object {

        /**
         * Generates a random memory.
         *
         * @param wordCount number of the words in the memory, 16 by default
         * @param wordSize number of bits in one word, 8 by default
         */
        fun random(wordCount: Int = 16, wordSize: Int = 8) =
            Ascm(wordSize, Array(wordCount) { BooleanArray(wordSize) { Random.nextBoolean() } })

    }

}