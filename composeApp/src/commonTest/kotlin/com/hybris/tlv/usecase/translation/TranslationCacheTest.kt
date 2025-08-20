package com.hybris.tlv.usecase.translation

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.translations
import com.hybris.tlv.usecase.translation.mapper.toTranslationCacheMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class TranslationCacheTest {

    private val mock = Mock()

    @Test
    fun `write and get translations`() = runBlocking {
        val translation = translations.first()
        val translationsMap = translations.toTranslationCacheMap()
        TranslationCache.set(languageIso = translation.languageIso)
        TranslationCache.set(translations = translationsMap)
        assertEquals(actual = translation.value, expected = TranslationCache.get(key = translation.key))
        assertEquals(actual = translation.value, expected = getTranslation(key = translation.key))
    }
}
