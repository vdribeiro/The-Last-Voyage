package com.hybris.tlv.domain.usecase.ship

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.hybris.tlv.domain.usecase.ship.model.Engine
import com.hybris.tlv.domain.usecase.ship.model.Ship
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase

internal class ShipUseCasesTest: TestCase() {

    @Test
    fun prepopulateAndSyncEngines() = runUnitTest {
        assertTrue(actual = getUseCases().ship.getEngines().isEmpty())
        getUseCases().ship.prepopulateEngines()
        assertEquals(expected = FakeData.getEngines().sortedBy { it.id }, actual = getUseCases().ship.getEngines().sortedBy { it.id })

        reset()
        assertTrue(actual = getUseCases().ship.getEngines().isEmpty())
        getUseCases().ship.syncEngines()
        assertEquals(expected = FakeData.getEngines().sortedBy { it.id }, actual = getUseCases().ship.getEngines().sortedBy { it.id })
    }

    @Test
    fun repairShip() = runUnitTest {
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
        val repairedShipIntegrity = getUseCases().ship.repairShip(ship = shipNoIntegrity)
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
        val repairedShipMaterials = getUseCases().ship.repairShip(ship = shipNoMaterials)
        assertEquals(expected = 9, actual = repairedShipMaterials.integrity)
        assertEquals(expected = 0, actual = repairedShipMaterials.materials)
    }
}
