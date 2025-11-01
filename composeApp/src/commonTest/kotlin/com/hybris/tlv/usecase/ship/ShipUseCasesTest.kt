package com.hybris.tlv.usecase.ship

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import com.hybris.tlv.reset
import com.hybris.tlv.testDependency
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship

internal class ShipUseCasesTest {

    @BeforeTest
    fun setup() = reset()

    @Test
    fun `sync and get engines`() = runBlocking {
        assertTrue(actual = testDependency.useCases.ship.getEngines().isEmpty())
        testDependency.useCases.ship.syncEngines()
        assertTrue(actual = testDependency.useCases.ship.getEngines().isNotEmpty())
    }

    @Test
    fun `repair ship`() = runBlocking {
        val shipNoIntegrity = Ship(
            id = "",
            engine = Engine(
                id = "",
                description = "",
                velocity = 0.1,
                fuelConsumption = 1.0,
                cost = 1
            ),
            assignedPoints = 0,
            yearsTraveled = 0.0,
            sensorRange = 5,
            integrity = 0,
            fuel = 10,
            materials = 50,
            cryopods = 50,
        )
        val repairedShipIntegrity = testDependency.useCases.ship.repairShip(ship = shipNoIntegrity)
        assertEquals(expected = 1, actual = repairedShipIntegrity.integrity)
        assertEquals(expected = 49, actual = repairedShipIntegrity.materials)
        val shipNoMaterials = Ship(
            id = "",
            engine = Engine(
                id = "",
                description = "",
                velocity = 0.1,
                fuelConsumption = 1.0,
                cost = 1
            ),
            assignedPoints = 0,
            yearsTraveled = 0.0,
            sensorRange = 5,
            integrity = 10,
            fuel = 10,
            materials = -1,
            cryopods = 50,
        )
        val repairedShipMaterials = testDependency.useCases.ship.repairShip(ship = shipNoMaterials)
        assertEquals(expected = 9, actual = repairedShipMaterials.integrity)
        assertEquals(expected = 0, actual = repairedShipMaterials.materials)
    }
}
