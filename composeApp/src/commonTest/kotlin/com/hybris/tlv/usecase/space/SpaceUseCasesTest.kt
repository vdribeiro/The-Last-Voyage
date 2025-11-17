package com.hybris.tlv.usecase.space

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.getUseCases
import com.hybris.tlv.reset
import com.hybris.tlv.stellarHosts

internal class SpaceUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get exoplanets`() = runBlocking {
        assertTrue(actual = getUseCases().space.getExoplanets().isEmpty())
        getUseCases().space.syncStellarHosts()
        getUseCases().space.syncPlanets()
        val stellarHosts = getUseCases().space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        assertTrue(actual = stellarHosts.map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `get stellar host`() = runBlocking {
        getUseCases().space.syncStellarHosts()
        val stellarHost = getUseCases().space.getStellarHost(id = "sol")
        assertEquals(expected = "sol", actual = stellarHost?.id)
    }

    @Test
    fun `get nearest stars`() = runBlocking {
        getUseCases().space.syncStellarHosts()
        val stellarHosts = getUseCases().space.getNearestStars(stellarHost = stellarHosts.first(), n = 1, visited = emptySet())
        assertEquals(expected = "proxima_cen", actual = stellarHosts.first().id)
    }
}
