package com.hybris.tlv.usecase.translation.local

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.translations
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class TranslationLocalTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `write and get translations`() = runBlocking {
        assertTrue(actual = mock.translationDao.isTranslationEmpty())
        mock.translationDao.rewriteTranslations(translations = translations)
        assertEquals(expected = translations, actual = mock.translationDao.getTranslations())
    }
}
