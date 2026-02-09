package com.hybris.tlv.domain.usecase.space

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class SpaceUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncExoplanets() = runUnitTest {
        assertTrue(actual = dependency.get().useCases.space.observeExoplanets().first().isEmpty())
        dependency.get().useCases.space.prepopulateStellarHosts()
        dependency.get().useCases.space.prepopulatePlanets()
        assertEquals(expected = FakeData.stellarHostsWithPlanets.get().sortedBy { it.id }, actual = dependency.get().useCases.space.observeExoplanets().first().sortedBy { it.id })

        reset()
        assertTrue(actual = dependency.get().useCases.space.observeExoplanets().first().isEmpty())
        dependency.get().useCases.space.syncStellarHosts()
        dependency.get().useCases.space.syncPlanets()
        assertEquals(expected = FakeData.stellarHostsWithPlanets.get().sortedBy { it.id }, actual = dependency.get().useCases.space.observeExoplanets().first().sortedBy { it.id })
    }

    @Test
    fun getStellarHost() = runUnitTest {
        dependency.get().useCases.space.prepopulateStellarHosts()
        val stellarHost = dependency.get().useCases.space.getStellarHost(id = SUN)
        assertEquals(expected = SUN, actual = stellarHost?.id)
    }

    @Test
    fun getNearestStars() = runUnitTest {
        dependency.get().useCases.space.prepopulateStellarHosts()
        dependency.get().useCases.space.prepopulatePlanets()
        val sun = FakeData.stellarHosts.get().first { it.id == SUN }
        val stellarHosts = dependency.get().useCases.space.getNearestStars(stellarHost = sun, n = 1, visited = emptySet())
        assertEquals(expected = "proxima_cen", actual = stellarHosts.first().id)
    }
}
