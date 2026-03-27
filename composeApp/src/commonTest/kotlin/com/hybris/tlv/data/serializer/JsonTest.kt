package com.hybris.tlv.data.serializer

import kotlin.test.Test
import kotlin.test.assertEquals
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class JsonTest: TestCase() {
    @Test
    fun encodeDecode() = runUnitTest {
        assertEquals(expected = FakeData.configs, actual = decode(value = encode(value = FakeData.configs)))
        assertEquals(expected = FakeData.configs, actual = decodeURL(value = encodeURL(value = FakeData.configs)))
    }
}
