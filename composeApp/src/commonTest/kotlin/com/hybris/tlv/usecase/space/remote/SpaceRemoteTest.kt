package com.hybris.tlv.usecase.space.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.mock.planets
import com.hybris.tlv.mock.stellarHosts
import com.hybris.tlv.usecase.space.remote.result.ExoplanetsResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class SpaceRemoteTest {

    @Test
    fun `get stellar hosts archive`() = runTest {
        val result = mock.spaceApi.getStellarHostsArchive()
        assertTrue(actual = result is ExoplanetsResult.Success)
        assertEquals(expected = stellarHosts, actual = result.stellarHosts)
    }

    @Test
    fun `get exoplanets archive`() = runTest {
        val result = mock.spaceApi.getExoplanetsArchive()
        assertTrue(actual = result is ExoplanetsResult.Success)
        assertEquals(expected = planets, actual = result.planets)
        val ids = planets.map { it.stellarHostId }
        val exoplanetHosts = stellarHosts.mapNotNull { if (it.id in ids) it.copy(systemName = null) else null }
        assertEquals(expected = exoplanetHosts, actual = result.stellarHosts.toSet().toList())
    }

    @Test
    fun `get K2 exoplanets archive`() = runTest {
        val result = mock.spaceApi.getK2ExoplanetsArchive()
        assertTrue(actual = result is ExoplanetsResult.Success)
        assertEquals(expected = planets, actual = result.planets)
        val ids = planets.map { it.stellarHostId }
        val exoplanetHosts = stellarHosts.mapNotNull { if (it.id in ids) it.copy(systemName = null) else null }
        assertEquals(expected = exoplanetHosts, actual = result.stellarHosts.toSet().toList())
    }

    @Test
    fun `get stellar hosts`() = runTest {
        assertEquals(expected = Result.Success(list = stellarHosts), actual = mock.spaceApi.getStellarHosts())
    }

    @Test
    fun `get planets`() = runTest {
        assertEquals(expected = Result.Success(list = planets), actual = mock.spaceApi.getPlanets())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.spaceApi.getStellarHostsArchive() is ExoplanetsResult.Error)
        assertTrue(actual = errorMock.spaceApi.getExoplanetsArchive() is ExoplanetsResult.Error)
        assertTrue(actual = errorMock.spaceApi.getK2ExoplanetsArchive() is ExoplanetsResult.Error)
        assertTrue(actual = errorMock.spaceApi.getStellarHosts() is Result.Error)
        assertTrue(actual = errorMock.spaceApi.getPlanets() is Result.Error)
    }
}
