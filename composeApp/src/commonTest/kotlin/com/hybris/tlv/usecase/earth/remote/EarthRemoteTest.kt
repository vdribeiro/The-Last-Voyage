package com.hybris.tlv.usecase.earth.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.catastrophes
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class EarthRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get catastrophes`() = runBlocking {
        assertEquals(expected = Result.Success(list = emptyList()), actual = mock.earthApi.getCatastrophes().last())
        assertEquals(expected = SyncResult.Success, actual = mock.earthApi.rewriteCatastrophes(catastrophes = catastrophes).last())
        assertEquals(expected = Result.Success(list = catastrophes), actual = mock.earthApi.getCatastrophes().last())
    }
}
