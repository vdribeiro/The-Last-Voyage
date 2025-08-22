package com.hybris.tlv.usecase.space

import com.hybris.tlv.mock.Mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class SpaceUseCasesTest {

    private val mock = Mock()

    @BeforeTest
    fun setup() {
        mock.clearDatabase()
    }

    @Test
    fun `prepopulate and get exoplanets`() = runBlocking {
        assertTrue(actual = mock.useCases.space.getExoplanets().isEmpty())
        mock.internalSpace.prepopulateStellarHosts()
        val stellarHosts = mock.useCases.space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        stellarHosts.forEach { assertTrue(actual = it.planets.isEmpty()) }
        mock.internalSpace.prepopulatePlanets()
        assertTrue(actual = mock.useCases.space.getExoplanets().map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `prepopulate and sync exoplanets`() = runBlocking {
        assertTrue(actual = mock.useCases.space.getExoplanets().isEmpty())
        mock.internalSpace.syncStellarHosts()
        val moreStellarHosts = mock.useCases.space.getExoplanets()
        assertTrue(actual = moreStellarHosts.isNotEmpty())
        moreStellarHosts.forEach { assertTrue(actual = it.planets.isEmpty()) }
        mock.internalSpace.syncPlanets()
        assertTrue(actual = mock.useCases.space.getExoplanets().map { it.planets }.flatten().isNotEmpty())
    }
}
