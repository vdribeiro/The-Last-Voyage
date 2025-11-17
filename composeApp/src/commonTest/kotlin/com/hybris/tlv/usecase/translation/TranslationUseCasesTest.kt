package com.hybris.tlv.usecase.translation

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset
import com.hybris.tlv.translations

internal class TranslationUseCasesTest {

    @BeforeTest
    fun setup() {
        reset()
        TranslationCache.set(translations = emptyList())
    }

    @Test
    fun `sync and get translations`() = runBlocking {
        val translation = translations.random()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
        getUseCases().translation.syncTranslations()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
    }
}
