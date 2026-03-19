package com.hybris.tlv.data.serializer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.data.resource.loadAllTranslationsFromJsonResource
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class JsonTest: TestCase() {
    @Test
    fun encodeDecode() = runUnitTest {
        assertEquals(expected = FakeData.configs, actual = decode(value = encode(value = FakeData.configs)))
        assertEquals(expected = FakeData.configs, actual = decodeURL(value = encodeURL(value = FakeData.configs)))
    }

    @Test
    fun saveLoad() = runUnitTest {
        assertTrue(actual = saveJsonFile(json = JsonFile.Configs, content = FakeData.configs))
        assertEquals(expected = FakeData.configs, actual = loadJsonFile(json = JsonFile.Configs))
        assertTrue(actual = deleteJsonFile(json = JsonFile.Configs))
        assertNull(actual = loadJsonFile(json = JsonFile.Configs))
    }

    @Test
    fun loadResource() = runUnitTest {
        assertEquals(expected = FakeData.translations.get(), actual = loadAllTranslationsFromJsonResource())
    }
}
