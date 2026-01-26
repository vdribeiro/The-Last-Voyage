package com.hybris.tlv.domain.usecase.translation

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class TranslationCacheTest: TestCase() {

    @Test
    fun writeAndGetTranslations() = runUnitTest {
        val translation = FakeData.translations.get().first()
        TranslationCache.set(translations = FakeData.translations.get())
        assertEquals(expected = translation.value, actual = TranslationCache.get(key = translation.key))
    }
}