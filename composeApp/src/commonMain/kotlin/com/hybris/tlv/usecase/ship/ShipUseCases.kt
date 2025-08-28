package com.hybris.tlv.usecase.ship

import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship

internal interface ShipUseCases {

    /**
     * Get all [Engine]s.
     */
    suspend fun getEngines(): List<Engine>

    /**
     * Repair ship.
     */
    suspend fun repairShip(ship: Ship): Ship
}
