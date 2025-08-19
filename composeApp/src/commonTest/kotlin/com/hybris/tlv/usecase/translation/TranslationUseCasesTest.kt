package com.hybris.tlv.usecase.translation

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.usecase.translation.TranslationCache.DEFAULT_LANGUAGE
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class TranslationUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and get translations`() = runBlocking {
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = DEFAULT_LANGUAGE).isEmpty())
        mock.internalTranslation.prepopulateTranslations()
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = DEFAULT_LANGUAGE).isNotEmpty())
    }

    @Test
    fun `rewrite and sync translations`() = runBlocking {
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = DEFAULT_LANGUAGE).isEmpty())
        mock.internalTranslation.rewriteTranslations().last()
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = DEFAULT_LANGUAGE).isNotEmpty())
        mock.clearDatabase()
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = DEFAULT_LANGUAGE).isEmpty())
        mock.internalTranslation.syncTranslations().last()
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = DEFAULT_LANGUAGE).isNotEmpty())
    }
}
