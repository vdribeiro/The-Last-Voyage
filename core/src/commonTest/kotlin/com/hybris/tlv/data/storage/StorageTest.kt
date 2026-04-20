package com.hybris.tlv.data.storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.hybris.tlv.test.FakeData

internal class StorageTest: TestCase() {
    @Test
    fun saveLoad() = runUnitTest {
        assertTrue(actual = saveJsonFile(path = FilePath.Configs, content = FakeData.configs))
        assertEquals(expected = FakeData.configs, actual = loadJsonFile(path = FilePath.Configs))
        assertTrue(actual = deleteJsonFile(path = FilePath.Configs))
        assertNull(actual = loadJsonFile(path = FilePath.Configs))
    }
}
