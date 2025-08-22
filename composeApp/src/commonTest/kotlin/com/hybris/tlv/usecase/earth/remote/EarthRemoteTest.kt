package com.hybris.tlv.usecase.earth.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.catastrophes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class EarthRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get catastrophes`() = runBlocking {
        assertEquals(expected = Result.Success(list = catastrophes), actual = mock.earthApi.getCatastrophes())
    }
}
