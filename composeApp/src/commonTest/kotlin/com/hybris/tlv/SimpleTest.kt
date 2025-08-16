package com.hybris.tlv

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimpleCommonTest {

    @Test
    fun `this is a sample test for checking addition`() {
        val expected = 4
        val actual = 2 + 2
        assertEquals(expected, actual, "Math should be correct!")
    }

    @Test
    fun `this is another sample test`() {
        assertTrue("KMP".contains("M"), "String should contain the letter 'M'")
    }
}
