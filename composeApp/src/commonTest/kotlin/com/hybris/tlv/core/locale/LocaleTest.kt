package com.hybris.tlv.core.locale

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import com.hybris.tlv.test.TestCase

internal class LocaleTest: TestCase() {
    @Test
    fun observeLocal() = runUnitTest {
        assertEquals(expected = getLanguage(), actual = observeLocale().first())
    }
}