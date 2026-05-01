package com.hybris.tlv.core.locale

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import com.hybris.tlv.test.TestCase

internal class LocaleTest: TestCase() {
    @Test
    fun observeLocal() = runUnitTest {
        assertEquals(expected = getLanguage(), actual = observeLocale().first())
    }

    @Test
    fun getLocalDateTimeValid() {
        assertTrue(actual = getLocalDateTime(utc = "2020-01-01T12:00:00Z").isNotEmpty())
        assertTrue(actual = getLocalDateTime(utc = "2020-01-01T12:00:00.500Z").isNotEmpty())
        assertTrue(actual = getLocalDateTime().isNotEmpty())
    }

    @Test
    fun getLocalDateTimeInvalid() {
        val invalid = "not-a-utc"
        assertEquals(expected = invalid, actual = getLocalDateTime(utc = invalid))
    }
}
