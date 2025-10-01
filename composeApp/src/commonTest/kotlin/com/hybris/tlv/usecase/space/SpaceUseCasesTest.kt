package com.hybris.tlv.usecase.space

import com.hybris.tlv.config.Configs
import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.testCore
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class SpaceUseCasesTest {

    @BeforeTest
    fun setup() {
        testCore.clearDatabase()
        testCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get exoplanets`() = runBlocking {
        assertTrue(actual = testCore.useCases.space.getExoplanets().isEmpty())
        testCore.useCases.space.syncStellarHosts()
        testCore.useCases.space.syncPlanets()
        val stellarHosts = testCore.useCases.space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        assertTrue(actual = stellarHosts.map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `prepopulate and get exoplanets`() = runBlocking {
        assertTrue(actual = testCore.useCases.space.getExoplanets().isEmpty())
        testCore.useCases.space.prepopulateStellarHosts()
        testCore.useCases.space.prepopulatePlanets()
        val stellarHosts = testCore.useCases.space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        assertTrue(actual = stellarHosts.map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `get stellar host`() = runBlocking {
        testCore.useCases.space.prepopulateStellarHosts()
        val stellarHost = testCore.useCases.space.getStellarHost(id = "sol")
        assertEquals(expected = "sol", actual = stellarHost?.id)
    }

    @Test
    fun `get nearest stars`() = runBlocking {
        testCore.useCases.space.prepopulateStellarHosts()
        val stellarHosts = testCore.useCases.space.getNearestStars(stellarHost = stellarHosts.first(), n = 1, visited = emptySet())
        assertEquals(expected = "proxima_centauri", actual = stellarHosts.first().id)
    }
}
