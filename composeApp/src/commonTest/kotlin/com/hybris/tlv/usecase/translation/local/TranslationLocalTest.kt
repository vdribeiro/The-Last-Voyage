package com.hybris.tlv.usecase.translation.local

import com.hybris.tlv.Tester
import com.hybris.tlv.mock.credits
import com.hybris.tlv.mock.engines
import com.hybris.tlv.mock.events
import com.hybris.tlv.mock.translations
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class TranslationLocalTest: Tester() {

    @Test
    fun `write and get translations`() = runBlocking {
        assertTrue(actual = translationDao.isTranslationEmpty())
        translationDao.rewriteTranslations(translations = translations)
        assertEquals(expected = translations, actual = translationDao.getTranslations())
    }
}
