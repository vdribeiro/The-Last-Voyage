package com.hybris.tlv.usecase.space

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock
import com.hybris.tlv.stellarHosts
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class SpaceUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
        mock.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get exoplanets`() = runBlocking {
        assertTrue(actual = mock.useCases.space.getExoplanets().isEmpty())
        mock.useCases.space.syncStellarHosts()
        mock.useCases.space.syncPlanets()
        val stellarHosts = mock.useCases.space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        assertTrue(actual = stellarHosts.map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `prepopulate and get exoplanets`() = runBlocking {
        assertTrue(actual = mock.useCases.space.getExoplanets().isEmpty())
        mock.useCases.space.prepopulateStellarHosts()
        mock.useCases.space.prepopulatePlanets()
        val stellarHosts = mock.useCases.space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        assertTrue(actual = stellarHosts.map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `get stellar host`() = runBlocking {
        mock.useCases.space.prepopulateStellarHosts()
        val stellarHost = mock.useCases.space.getStellarHost(id = "sol")
        assertEquals(expected = "sol", actual = stellarHost?.id)
    }

    @Test
    fun `get nearest stars`() = runBlocking {
        mock.useCases.space.prepopulateStellarHosts()
        val stellarHosts = mock.useCases.space.getNearestStars(stellarHost = stellarHosts.first(), n = 1, visited = emptySet())
        assertEquals(expected = "proxima_centauri", actual = stellarHosts.first().id)
    }
}
