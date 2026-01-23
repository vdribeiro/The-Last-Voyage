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
        assertTrue(actual = getUseCases().space.observeExoplanets().first().isEmpty())
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        assertEquals(expected = FakeData.getHostsWithPlanets().sortedBy { it.id }, actual = getUseCases().space.observeExoplanets().first().sortedBy { it.id })

        reset()
        assertTrue(actual = getUseCases().space.observeExoplanets().first().isEmpty())
        getUseCases().space.syncStellarHosts()
        getUseCases().space.syncPlanets()
        assertEquals(expected = FakeData.getHostsWithPlanets().sortedBy { it.id }, actual = getUseCases().space.observeExoplanets().first().sortedBy { it.id })
    }

    @Test
    fun getStellarHost() = runUnitTest {
        getUseCases().space.prepopulateStellarHosts()
        val stellarHost = getUseCases().space.getStellarHost(id = SUN)
        assertEquals(expected = SUN, actual = stellarHost?.id)
    }

    @Test
    fun getNearestStars() = runUnitTest {
        getUseCases().space.prepopulateStellarHosts()
        getUseCases().space.prepopulatePlanets()
        val sun = FakeData.getStellarHosts().first { it.id == SUN }
        val stellarHosts = getUseCases().space.getNearestStars(stellarHost = sun, n = 1, visited = emptySet())
        assertEquals(expected = "proxima_cen", actual = stellarHosts.first().id)
    }
}
