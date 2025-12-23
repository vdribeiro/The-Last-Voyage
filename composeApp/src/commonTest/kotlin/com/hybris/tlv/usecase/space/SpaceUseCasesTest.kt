package com.hybris.tlv.usecase.space

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import com.hybris.tlv.TestCase
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.stellarHosts

internal class SpaceUseCasesTest: TestCase() {

    @Test
    fun `prepopulate and sync exoplanets`() = runUnitTest {
        assertTrue(actual = useCases.space.observeExoplanets().first().isEmpty())
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        assertEquals(expected = hostsWithPlanets.sortedBy { it.id }, actual = useCases.space.observeExoplanets().first().sortedBy { it.id })

        reset()
        assertTrue(actual = useCases.space.observeExoplanets().first().isEmpty())
        useCases.space.syncStellarHosts()
        useCases.space.syncPlanets()
        assertEquals(expected = hostsWithPlanets.sortedBy { it.id }, actual = useCases.space.observeExoplanets().first().sortedBy { it.id })
    }

    @Test
    fun `get stellar host`() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        val stellarHost = useCases.space.getStellarHost(id = SUN)
        assertEquals(expected = SUN, actual = stellarHost?.id)
    }

    @Test
    fun `get nearest stars`() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        val sun = stellarHosts.first { it.id == SUN }
        val stellarHosts = useCases.space.getNearestStars(stellarHost = sun, n = 1, visited = emptySet())
        assertEquals(expected = "proxima_cen", actual = stellarHosts.first().id)
    }
}
