package com.hybris.tlv.usecase.space

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import com.hybris.tlv.TestCase
import com.hybris.tlv.stellarHosts

internal class SpaceUseCasesTest: TestCase() {

    @Test
    fun `sync and get exoplanets`() = runUnitTest {
        assertTrue(actual = useCases.space.observeExoplanets().first().isEmpty())
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        val stellarHosts = useCases.space.observeExoplanets().first()
        assertTrue(actual = stellarHosts.isNotEmpty())
        assertTrue(actual = stellarHosts.map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `get stellar host`() = runUnitTest {
        useCases.space.syncStellarHosts()
        val stellarHost = useCases.space.getStellarHost(id = "sol")
        assertEquals(expected = "sol", actual = stellarHost?.id)
    }

    @Test
    fun `get nearest stars`() = runUnitTest {
        useCases.space.syncStellarHosts()
        val stellarHosts = useCases.space.getNearestStars(stellarHost = stellarHosts.first(), n = 1, visited = emptySet())
        assertEquals(expected = "proxima_cen", actual = stellarHosts.first().id)
    }
}
