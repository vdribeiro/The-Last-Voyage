package com.hybris.tlv.usecase.translation

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testDependency
import com.hybris.tlv.translations
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class TranslationUseCasesTest {

    @BeforeTest
    fun setup() {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.localConfigs = Configs()
        TranslationCache.set(emptyList())
    }

    @Test
    fun `sync and get translations`() = runBlocking {
        val translation = translations.random()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
        testDependency.useCases.translation.syncTranslations()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
    }

    @Test
    fun `prepopulate and get translations`() = runBlocking {
        val translation = translations.random()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
        testDependency.useCases.translation.prepopulateTranslations()
        assertEquals(expected = translation.value, actual = TranslationCache.get(translation.key))
    }
}
