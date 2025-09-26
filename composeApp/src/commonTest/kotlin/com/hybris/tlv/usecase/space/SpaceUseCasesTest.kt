package com.hybris.tlv.usecase.space

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mockCore
import com.hybris.tlv.stellarHosts
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class SpaceUseCasesTest {

    @BeforeTest
    fun setup() {
        mockCore.sqlDriver.clearDatabase()
        mockCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get exoplanets`() = runBlocking {
        assertTrue(actual = mockCore.useCases.space.getExoplanets().isEmpty())
        mockCore.useCases.space.syncStellarHosts()
        mockCore.useCases.space.syncPlanets()
        val stellarHosts = mockCore.useCases.space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        assertTrue(actual = stellarHosts.map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `prepopulate and get exoplanets`() = runBlocking {
        assertTrue(actual = mockCore.useCases.space.getExoplanets().isEmpty())
        mockCore.useCases.space.prepopulateStellarHosts()
        mockCore.useCases.space.prepopulatePlanets()
        val stellarHosts = mockCore.useCases.space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        assertTrue(actual = stellarHosts.map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `get stellar host`() = runBlocking {
        mockCore.useCases.space.prepopulateStellarHosts()
        val stellarHost = mockCore.useCases.space.getStellarHost(id = "sol")
        assertEquals(expected = "sol", actual = stellarHost?.id)
    }

    @Test
    fun `get nearest stars`() = runBlocking {
        mockCore.useCases.space.prepopulateStellarHosts()
        val stellarHosts = mockCore.useCases.space.getNearestStars(stellarHost = stellarHosts.first(), n = 1, visited = emptySet())
        assertEquals(expected = "proxima_centauri", actual = stellarHosts.first().id)
    }
}
