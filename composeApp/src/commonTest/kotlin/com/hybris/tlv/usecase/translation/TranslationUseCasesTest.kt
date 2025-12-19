package com.hybris.tlv.usecase.translation

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.TestCase
import com.hybris.tlv.translations

internal class TranslationUseCasesTest: TestCase() {

    @Test
    fun `sync and get translations`() = runUnitTest {
        TranslationCache.set(translations = emptyList())
        val translation = translations.random()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
        useCases.translation.syncTranslations()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
    }
}
