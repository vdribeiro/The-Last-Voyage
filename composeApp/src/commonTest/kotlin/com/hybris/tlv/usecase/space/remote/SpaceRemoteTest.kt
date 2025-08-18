package com.hybris.tlv.usecase.space.remote

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.space.remote.result.ExoplanetsResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class SpaceRemoteTest {

    private val mock = Mock()

    @Test
    fun `get stellar hosts archive`() = runBlocking {
        val result = mock.spaceApi.getStellarHostsArchive()
        assertTrue(actual = result is ExoplanetsResult.Success)
        assertEquals(expected = stellarHosts, actual = result.stellarHosts)
    }

    @Test
    fun `write and get stellar hosts`() = runBlocking {
        assertEquals(expected = Result.Success(list = emptyList()), actual = mock.spaceApi.getStellarHosts().last())
        assertEquals(expected = SyncResult.Success, actual = mock.spaceApi.rewriteStellarHosts(stellarHosts = stellarHosts).last())
        assertEquals(expected = Result.Success(list = stellarHosts), actual = mock.spaceApi.getStellarHosts().last())
    }

    @Test
    fun `write and get planets`() = runBlocking {
        assertEquals(expected = Result.Success(list = emptyList()), actual = mock.spaceApi.getPlanets().last())
        assertEquals(expected = SyncResult.Success, actual = mock.spaceApi.rewritePlanets(planets = planets).last())
        assertEquals(expected = Result.Success(list = planets), actual = mock.spaceApi.getPlanets().last())
    }
}
