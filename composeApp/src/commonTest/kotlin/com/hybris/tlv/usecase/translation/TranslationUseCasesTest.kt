package com.hybris.tlv.usecase.translation

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.TestCase
import com.hybris.tlv.translations

internal class TranslationUseCasesTest: TestCase() {

    @Test
    fun `sync and get translations`() = runUnitTest {
        // Get map
        TranslationCache.set(translations = translations)
        val translations = TranslationCache.cacheState.value.translations
        TranslationCache.reset()

        useCases.translation.prepopulateTranslations()
        useCases.translation.refreshCache()
        assertEquals(expected = translations, actual = TranslationCache.cacheState.value.translations)

        TranslationCache.reset()
        useCases.translation.syncTranslations()
        useCases.translation.refreshCache()
        assertEquals(expected = translations, actual = TranslationCache.cacheState.value.translations)
    }
}
