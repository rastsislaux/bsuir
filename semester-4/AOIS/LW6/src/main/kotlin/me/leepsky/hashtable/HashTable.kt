package me.leepsky.hashtable

import java.util.LinkedList
import kotlin.math.abs

class HashTable<K, V>(private val cap: Int): Iterable<Pair<K, V>> {

    private val data = Array<LinkedList<Pair<K, V>>>(cap) { LinkedList() }

    fun index(key: K) = abs(key.hashCode()) % cap

    operator fun set(key: K, value: V) {
        val list = data[index(key)]
        for (pair in list) if (pair.key == key) {
            pair.value = value
            return
        }
        list.add(Pair(key, value))
    }

    operator fun get(key: K): V? {
        val list = data[index(key)]
        for (pair in list) if (pair.key == key) return pair.value
        return null
    }

    fun containsKey(key: K) = get(key) != null

    fun remove(key: K) {
        val iterator = data[index(key)].iterator()
        while (iterator.hasNext()) {
            val pair = iterator.next()
            if (pair.key == key) {
                iterator.remove()
                return
            }
        }
    }

    val size: Int
        get() = data.sumOf { it.size }

    val coefficient: Double
        get() = data.count { !it.isEmpty() } / cap.toDouble()

    /**
     * Returns an iterator over the elements of this object.
     */
    override fun iterator() = data.flatMap { it }.iterator()

}

data class Pair<K, V>(
    val key: K,
    var value: V
)
