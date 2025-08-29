package com.hybris.tlv.usecase.space

import com.hybris.tlv.database.clearDatabase
import com.hybris.tlv.mock.errorMock
import com.hybris.tlv.mock.mock
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class SpaceUseCasesTest {

    @BeforeTest
    fun setup() {
        mock.sqlDriver.clearDatabase()
    }

    @Test
    fun `get archive`() = runTest {

    }

    @Test
    fun `prepopulate and get exoplanets`() = runTest {
        assertTrue(actual = mock.useCases.space.getExoplanets().isEmpty())
        mock.internalSpace.prepopulateStellarHosts()
        val stellarHosts = mock.useCases.space.getExoplanets()
        assertTrue(actual = stellarHosts.isNotEmpty())
        stellarHosts.forEach { assertTrue(actual = it.planets.isEmpty()) }
        mock.internalSpace.prepopulatePlanets()
        assertTrue(actual = mock.useCases.space.getExoplanets().map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `prepopulate and sync exoplanets`() = runTest {
        assertTrue(actual = mock.useCases.space.getExoplanets().isEmpty())
        assertTrue(actual = mock.internalSpace.syncStellarHosts() is SyncResult.Success)
        val moreStellarHosts = mock.useCases.space.getExoplanets()
        assertTrue(actual = moreStellarHosts.isNotEmpty())
        moreStellarHosts.forEach { assertTrue(actual = it.planets.isEmpty()) }
        assertTrue(actual = mock.internalSpace.syncPlanets() is SyncResult.Success)
        assertTrue(actual = mock.useCases.space.getExoplanets().map { it.planets }.flatten().isNotEmpty())
    }

    @Test
    fun `get error`() = runTest {
        assertTrue(actual = errorMock.internalSpace.syncStellarHosts() is SyncResult.Error)
        assertTrue(actual = errorMock.internalSpace.syncPlanets() is SyncResult.Error)
    }
}
