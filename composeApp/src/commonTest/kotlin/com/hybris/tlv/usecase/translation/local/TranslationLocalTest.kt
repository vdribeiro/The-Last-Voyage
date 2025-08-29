package com.hybris.tlv.usecase.translation.local

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.mock
import com.hybris.tlv.mock.translations
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class TranslationLocalTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `write and get translations`() = runTest {
        assertTrue(actual = mock.translationDao.isTranslationEmpty())
        mock.translationDao.rewriteTranslations(translations = translations)
        assertEquals(expected = translations, actual = mock.translationDao.getTranslations())
    }
}
