package com.hybris.tlv.usecase.earth.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.catastrophes
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class EarthRemoteTest {

    @Test
    fun `get catastrophes`() = runTest {
        assertEquals(expected = Result.Success(list = catastrophes), actual = mock.earthApi.getCatastrophes())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.earthApi.getCatastrophes() is Result.Error)
    }
}
