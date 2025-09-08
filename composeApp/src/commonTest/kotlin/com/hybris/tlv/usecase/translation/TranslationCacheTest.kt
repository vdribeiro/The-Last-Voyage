package com.hybris.tlv.usecase.translation

import com.hybris.tlv.mock.translations
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class TranslationCacheTest {

    @Test
    fun `write and get translations`() = runBlocking {
        val translation = translations.first()
        TranslationCache.set(translations = translations)
        assertEquals(expected = translation.value, actual = TranslationCache.get(key = translation.key))
        assertEquals(expected = translation.value, actual = getTranslation(key = translation.key))
    }
}
