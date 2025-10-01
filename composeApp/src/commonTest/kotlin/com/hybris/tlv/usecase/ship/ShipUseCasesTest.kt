package com.hybris.tlv.usecase.ship

import com.hybris.tlv.config.Configs
import com.hybris.tlv.testCore
import com.hybris.tlv.usecase.ship.model.Ship
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

internal class ShipUseCasesTest {

    @BeforeTest
    fun setup() {
        testCore.clearDatabase()
        testCore.config.localConfigs = Configs()
    }

    @Test
    fun `sync and get engines`() = runBlocking {
        assertTrue(actual = testCore.useCases.ship.getEngines().isEmpty())
        testCore.useCases.ship.syncEngines()
        assertTrue(actual = testCore.useCases.ship.getEngines().isNotEmpty())
    }

    @Test
    fun `prepopulate and get engines`() = runBlocking {
        assertTrue(actual = testCore.useCases.ship.getEngines().isEmpty())
        testCore.useCases.ship.prepopulateEngines()
        assertTrue(actual = testCore.useCases.ship.getEngines().isNotEmpty())
    }

    @Test
    fun `repair ship`() = runBlocking {
        val shipNoIntegrity = Ship(
            id = "",
            assignedPoints = 0,
            yearsTraveled = 0.0,
            sensorRange = 5,
            integrity = 0,
            fuel = 10,
            materials = 50,
            cryopods = 50,
        )
        val repairedShipIntegrity = testCore.useCases.ship.repairShip(ship = shipNoIntegrity)
        assertEquals(expected = 1, actual = repairedShipIntegrity.integrity)
        assertEquals(expected = 49, actual = repairedShipIntegrity.materials)
        val shipNoMaterials = Ship(
            id = "",
            assignedPoints = 0,
            yearsTraveled = 0.0,
            sensorRange = 5,
            integrity = 10,
            fuel = 10,
            materials = -1,
            cryopods = 50,
        )
        val repairedShipMaterials = testCore.useCases.ship.repairShip(ship = shipNoMaterials)
        assertEquals(expected = 9, actual = repairedShipMaterials.integrity)
        assertEquals(expected = 0, actual = repairedShipMaterials.materials)
    }
}
