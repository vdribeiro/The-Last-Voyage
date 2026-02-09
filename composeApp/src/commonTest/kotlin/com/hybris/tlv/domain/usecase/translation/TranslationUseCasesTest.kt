package com.hybris.tlv.domain.usecase.translation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class TranslationUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncTranslations() = runUnitTest {
        assertTrue(actual = dependency.get().useCases.translation.getTranslations().isEmpty())
        dependency.get().useCases.translation.prepopulateTranslations()
        assertEquals(expected = FakeData.translations.get().sortedBy { it.key }, actual = dependency.get().useCases.translation.getTranslations().sortedBy { it.key })

        reset()
        assertTrue(actual = dependency.get().useCases.translation.getTranslations().isEmpty())
        dependency.get().useCases.translation.syncTranslations()
        assertEquals(expected = FakeData.translations.get().sortedBy { it.key }, actual = dependency.get().useCases.translation.getTranslations().sortedBy { it.key })
    }
}
