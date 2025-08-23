package com.hybris.tlv.usecase.ship

import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import kotlin.math.abs

internal class ShipGateway(
    private val shipDao: ShipLocal
): ShipUseCases {

    override suspend fun getEngines(): List<Engine> =
        shipDao.getEngines().sortedByDescending { it.velocity }

    override suspend fun repairShip(ship: Ship): Ship {
        var integrity = ship.integrity
        var materials = ship.materials
        val fuel = if (ship.fuel < 0) 0 else ship.fuel
        val cryopods = if (ship.cryopods < 0) 0 else ship.cryopods

        if (integrity <= 0) {
            // Attempt to repair the ship
            val repairAmount = abs(n = integrity) + 1
            if (materials >= repairAmount) {
                integrity = 1
                materials -= repairAmount
            } else {
                integrity = 0
                materials = 0
            }
        }

        if (materials < 0) {
            // Equalize loss
            val materialDeficit = abs(n = materials)
            integrity = if (integrity > materialDeficit) integrity - materialDeficit else 0
            materials = 0
        }

        return ship.copy(
            integrity = integrity,
            materials = materials,
            fuel = fuel,
            cryopods = cryopods
        )
    }
}
