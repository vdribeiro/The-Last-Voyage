package com.hybris.tlv.domain.usecase.translation

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.test.translations

internal class TranslationCacheTest: TestCase() {

    @Test
    fun writeAndGetTranslations() = runUnitTest {
        val translation = translations.first()
        TranslationCache.set(translations = translations)
        assertEquals(expected = translation.value, actual = TranslationCache.get(key = translation.key))
    }
}