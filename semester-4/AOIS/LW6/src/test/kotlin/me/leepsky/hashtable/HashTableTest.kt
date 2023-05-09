package me.leepsky.hashtable

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HashTableTest {

    @Test
    fun `put() must put elements`() {
        val ht = HashTable<String, String>(16)
        ht["First"] = "Second"
        ht["Second"] = "Third"

        assertAll (
            { assertEquals("Second", ht["First"]) },
            { assertEquals("Third", ht["Second"]) }
        )
    }

    @Test
    fun `size() must be correct`() {
        val ht = HashTable<String, String>(16)
        ht["First"] = "Second"
        ht["Second"] = "Third"

        assertAll (
            { assertEquals(2, ht.size) }
        )
    }

    @Test
    fun `remove() must remove`() {
        val ht = HashTable<String, String>(16)
        ht["First"] = "Second"
        ht["Second"] = "Third"

        ht.remove("First")

        assertAll(
            { assertEquals(1, ht.size) },
            { assertEquals("Third", ht["Second"]) },
            { assertNull(ht["First"]) }
        )
    }

    @Test
    fun `iterator must work`() {
        val ht = HashTable<String, String>(16)
        ht["1"] = "1"
        ht["2"] = "2"
        ht["3"] = "3"
        ht["4"] = "4"
        ht["5"] = "5"
        ht["6"] = "6"

        val values = ht.map { it.value }
        assertAll(
            { assertTrue { values.contains("1") } },
            { assertTrue { values.contains("2") } },
            { assertTrue { values.contains("3") } },
            { assertTrue { values.contains("4") } },
            { assertTrue { values.contains("5") } },
            { assertTrue { values.contains("6") } },
            { assertTrue { values.size == 6 } }
        )
    }

}