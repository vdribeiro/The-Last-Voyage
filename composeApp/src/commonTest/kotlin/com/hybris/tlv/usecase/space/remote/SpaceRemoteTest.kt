package com.hybris.tlv.usecase.space.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.usecase.space.remote.result.ExoplanetsResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
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
    fun `get exoplanets archive`() = runBlocking {
        val result = mock.spaceApi.getExoplanetsArchive()
        assertTrue(actual = result is ExoplanetsResult.Success)
        assertEquals(expected = planets, actual = result.planets)
        val ids = planets.map { it.stellarHostId }
        val exoplanetHosts = stellarHosts.mapNotNull { if (it.id in ids) it.copy(systemName = null) else null }
        assertEquals(expected = exoplanetHosts, actual = result.stellarHosts.toSet().toList())
    }

    @Test
    fun `get K2 exoplanets archive`() = runBlocking {
        val result = mock.spaceApi.getK2ExoplanetsArchive()
        assertTrue(actual = result is ExoplanetsResult.Success)
        assertEquals(expected = planets, actual = result.planets)
        val ids = planets.map { it.stellarHostId }
        val exoplanetHosts = stellarHosts.mapNotNull { if (it.id in ids) it.copy(systemName = null) else null }
        assertEquals(expected = exoplanetHosts, actual = result.stellarHosts.toSet().toList())
    }

    @Test
    fun `write and get stellar hosts`() = runBlocking {
        assertEquals(expected = Result.Success(list = stellarHosts), actual = mock.spaceApi.getStellarHosts())
    }

    @Test
    fun `write and get planets`() = runBlocking {
        assertEquals(expected = Result.Success(list = planets), actual = mock.spaceApi.getPlanets())
    }
}
