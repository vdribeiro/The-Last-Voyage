package com.hybris.tlv.domain.usecase.translation

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.TestCase
import com.hybris.tlv.translations

internal class TranslationCacheTest: TestCase() {

    @Test
    fun writeAndGetTranslations() = runUnitTest {
        val translation = translations.first()
        TranslationCache.set(translations = translations)
        assertEquals(expected = translation.value, actual = TranslationCache.get(key = translation.key))
    }
}