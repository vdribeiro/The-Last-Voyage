package com.hybris.tlv.domain.usecase.translation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class TranslationUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncTranslations() = runUnitTest {
        assertTrue(actual = useCases.translation.getTranslations().isEmpty())
        useCases.translation.prepopulateTranslations()
        assertEquals(expected = FakeData.getTranslations().sortedBy { it.key }, actual = useCases.translation.getTranslations().sortedBy { it.key })

        reset()
        assertTrue(actual = useCases.translation.getTranslations().isEmpty())
        useCases.translation.syncTranslations()
        assertEquals(expected = FakeData.getTranslations().sortedBy { it.key }, actual = useCases.translation.getTranslations().sortedBy { it.key })
    }
}
