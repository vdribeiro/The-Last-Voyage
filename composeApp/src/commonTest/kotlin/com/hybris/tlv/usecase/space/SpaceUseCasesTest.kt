package com.hybris.tlv.usecase.space

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.reset
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.testDependency

internal class SpaceUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get exoplanets`() = runBlocking {
        assertTrue(actual = testDependency.useCases.space.getExoplanets().isEmpty())
        testDependency.useCases.space.syncStellarHosts()
        testDependency.useCases.space.syncPlanets()
        val stellarHosts = testDependency.useCases.space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        assertTrue(actual = stellarHosts.map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `get stellar host`() = runBlocking {
        testDependency.useCases.space.syncStellarHosts()
        val stellarHost = testDependency.useCases.space.getStellarHost(id = "sol")
        assertEquals(expected = "sol", actual = stellarHost?.id)
    }

    @Test
    fun `get nearest stars`() = runBlocking {
        testDependency.useCases.space.syncStellarHosts()
        val stellarHosts = testDependency.useCases.space.getNearestStars(stellarHost = stellarHosts.first(), n = 1, visited = emptySet())
        assertEquals(expected = "proxima_cen", actual = stellarHosts.first().id)
    }
}
