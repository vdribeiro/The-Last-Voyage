package com.hybris.tlv.usecase.translation

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.mock.translations
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class TranslationUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `prepopulate and get translations`() = runBlocking {
        translations.first().languageIso
        assertTrue(actual = mock.translationDao.getTranslations().isEmpty())
        mock.internalTranslation.prepopulateTranslations()
        assertTrue(actual = mock.translationDao.getTranslations().isNotEmpty())
    }

    @Test
    fun `prepopulate and sync translations`() = runBlocking {
        translations.first().languageIso
        assertTrue(actual = mock.translationDao.getTranslations().isEmpty())
        assertTrue(actual = mock.internalTranslation.syncTranslations() is SyncResult.Success)
        assertTrue(actual = mock.translationDao.getTranslations().isNotEmpty())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.internalTranslation.syncTranslations() is SyncResult.Error)
    }
}
