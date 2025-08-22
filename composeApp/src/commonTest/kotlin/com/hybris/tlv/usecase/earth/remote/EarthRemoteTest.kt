package com.hybris.tlv.usecase.earth.remote

import com.hybris.tlv.http.HttpClientFactory
import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.catastrophes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class EarthRemoteTest {

    private val mock = Mock()
    private val errorMock = Mock(httpClient = HttpClientFactory.buildErrorHttpClient())

    @Test
    fun `get catastrophes`() = runBlocking {
        assertEquals(expected = Result.Success(list = catastrophes), actual = mock.earthApi.getCatastrophes())
    }

    @Test
    fun `get error`() = runBlocking {
        assertTrue(actual = errorMock.earthApi.getCatastrophes() is Result.Error)
    }
}
