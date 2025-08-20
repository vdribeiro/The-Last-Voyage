package com.hybris.tlv.usecase.translation

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.translations
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
        val languageIso = translations.first().languageIso
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = languageIso).isEmpty())
        mock.internalTranslation.prepopulateTranslations()
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = languageIso).isNotEmpty())
    }

    @Test
    fun `rewrite and sync translations`() = runBlocking {
        val languageIso = translations.first().languageIso
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = languageIso).isEmpty())
        mock.internalTranslation.rewriteTranslations().last()
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = languageIso).isNotEmpty())
        mock.clearDatabase()
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = languageIso).isEmpty())
        mock.internalTranslation.syncTranslations().last()
        assertTrue(actual = mock.internalTranslation.loadTranslationsToCache(languageIso = languageIso).isNotEmpty())
    }
}
