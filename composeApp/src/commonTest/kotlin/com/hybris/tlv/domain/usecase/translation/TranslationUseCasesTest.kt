package com.hybris.tlv.domain.usecase.translation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class TranslationUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncTranslations() = runUnitTest {
        assertTrue(actual = getUseCases().translation.getTranslations().isEmpty())
        getUseCases().translation.prepopulateTranslations()
        assertEquals(expected = FakeData.translations.get().sortedBy { it.key }, actual = getUseCases().translation.getTranslations().sortedBy { it.key })

        reset()
        assertTrue(actual = getUseCases().translation.getTranslations().isEmpty())
        getUseCases().translation.syncTranslations()
        assertEquals(expected = FakeData.translations.get().sortedBy { it.key }, actual = getUseCases().translation.getTranslations().sortedBy { it.key })
    }
}
