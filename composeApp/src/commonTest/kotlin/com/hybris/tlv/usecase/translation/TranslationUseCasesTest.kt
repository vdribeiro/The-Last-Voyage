package com.hybris.tlv.usecase.translation

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.usecase.translation.TranslationCache.DEFAULT_LANGUAGE
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.runBlocking

internal class TranslationUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and get translations`() = runBlocking {
        val translations = mock.internalTranslation.loadTranslationsToCache(languageIso = DEFAULT_LANGUAGE)

        //assertTrue(actual = mock.useCases.translation.getTranslations().isEmpty())
        //mock.internalTranslation.prepopulateTranslations()
        //assertTrue(actual = mock.useCases.translation.getTranslations().isNotEmpty())
    }

    @Test
    fun `rewrite and sync translations`() = runBlocking {
        //assertTrue(actual = mock.useCases.translation.getTranslations().isEmpty())
        //mock.internalTranslation.rewriteTranslations().last()
        //assertTrue(actual = mock.useCases.translation.getTranslations().isNotEmpty())
        //mock.clearDatabase()
        //assertTrue(actual = mock.useCases.translation.getTranslations().isEmpty())
        //mock.internalTranslation.syncTranslations().last()
        //assertTrue(actual = mock.useCases.translation.getTranslations().isNotEmpty())
    }
}
