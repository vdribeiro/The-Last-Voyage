package com.hybris.tlv.usecase.space

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.first
import com.hybris.tlv.TestCase
import com.hybris.tlv.hostsWithPlanets
import com.hybris.tlv.stellarHosts
import com.hybris.tlv.usecase.space.model.PlanetType

internal class SpaceUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncExoplanets() = runUnitTest {
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
    fun getStellarHost() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        val stellarHost = useCases.space.getStellarHost(id = SUN)
        assertEquals(expected = SUN, actual = stellarHost?.id)
    }

    @Test
    fun getNearestStars() = runUnitTest {
        useCases.space.prepopulateStellarHosts()
        useCases.space.prepopulatePlanets()
        val sun = stellarHosts.first { it.id == SUN }
        val stellarHosts = useCases.space.getNearestStars(stellarHost = sun, n = 1, visited = emptySet())
        assertEquals(expected = "proxima_cen", actual = stellarHosts.first().id)
    }

    @Test
    fun getPlanetType() = runUnitTest {
        assertEquals(expected = PlanetType.EARTH_LIKE_PLANET, actual = PlanetType.fromValue(value = "EarTH_LikE_pLANet"))
    }
}
