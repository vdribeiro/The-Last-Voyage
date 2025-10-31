package com.hybris.tlv.usecase.translation

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.testDependency
import com.hybris.tlv.translations

internal class TranslationUseCasesTest {

    @BeforeTest
    fun setup() {
        testDependency.sqlDriver.clearDatabase()
        testDependency.config.resetLocalConfigs()
        TranslationCache.set(emptyList())
    }

    @Test
    fun `sync and get translations`() = runBlocking {
        val translation = translations.random()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
        testDependency.useCases.translation.syncTranslations()
        assertEquals(expected = translation.key, actual = TranslationCache.get(translation.key))
    }
}
