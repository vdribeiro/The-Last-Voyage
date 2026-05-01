package com.hybris.tlv.core.security

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import com.hybris.tlv.test.TestCase

internal class UuidTest: TestCase() {

    private val uuidRegex = Regex(pattern = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")

    private fun testUuid(uuid: String): String {
        assertEquals(expected = 36, actual = uuid.length)
        assertTrue(actual = uuid.matches(regex = uuidRegex))
        return uuid
    }

    @Test
    fun generateUuid() = runUnitTest {
        val uuid = testUuid(uuid = uuid())
        assertNotEquals(illegal = uuid, actual = testUuid(uuid = uuid()))
    }

    @Test
    fun generateUuidV7() = runUnitTest {
        val uuid = testUuid(uuid = uuidV7()!!)
        assertEquals(expected = '7', actual = uuid[14])
        assertNotEquals(illegal = uuid, actual = testUuid(uuid = uuidV7()!!))
    }

    @Test
    fun generateUuidV4() = runUnitTest {
        val uuid = testUuid(uuid = uuidV4()!!)
        assertEquals(expected = '4', actual = uuid[14])
        assertNotEquals(illegal = uuid, actual = testUuid(uuid = uuidV4()!!))
    }

    @Test
    fun generateUnsecureUuid() = runUnitTest {
        val uuid = testUuid(uuid = unsecureUuid())
        assertEquals(expected = '7', actual = uuid[14])
        assertTrue(actual = uuid[19] in listOf('8', '9', 'a', 'b'))
        assertNotEquals(illegal = uuid, actual = testUuid(uuid = unsecureUuid()))
    }
}
