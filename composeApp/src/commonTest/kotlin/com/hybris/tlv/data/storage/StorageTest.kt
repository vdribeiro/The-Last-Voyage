package com.hybris.tlv.data.storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.domain.usecase.translation.TranslationGateway.Companion.loadAllTranslationsFromJsonResource
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class StorageTest: TestCase() {
    @Test
    fun saveLoad() = runUnitTest {
        assertTrue(actual = saveJsonFile(json = FilePath.Configs, content = FakeData.configs))
        assertEquals(expected = FakeData.configs, actual = loadJsonFile(json = FilePath.Configs))
        assertTrue(actual = deleteJsonFile(json = FilePath.Configs))
        assertNull(actual = loadJsonFile(json = FilePath.Configs))
    }

    @Test
    fun loadResource() = runUnitTest {
        assertEquals(expected = FakeData.translations.get(), actual = loadAllTranslationsFromJsonResource())
    }
}
