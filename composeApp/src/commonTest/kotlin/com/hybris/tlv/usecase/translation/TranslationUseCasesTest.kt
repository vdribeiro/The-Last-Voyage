package com.hybris.tlv.usecase.translation

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import com.hybris.tlv.translations
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class TranslationUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `sync and get translations`() = runBlocking {
        val translation = translations.random()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
        mock.useCases.translation.syncTranslations()
        assertEquals(expected = translation.value, actual = TranslationCache.get(translation.key))
    }

    @Test
    fun `prepopulate and get translations`() = runBlocking {
        val translation = translations.random()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
        mock.useCases.translation.prepopulateTranslations()
        assertEquals(expected = translation.value, actual = TranslationCache.get(translation.key))
    }
}
