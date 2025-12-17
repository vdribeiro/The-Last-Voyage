package com.hybris.tlv.usecase.ship

import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship

internal interface ShipUseCases {

    /**
     * Sync [Engine]s.
     */
    suspend fun syncEngines(): Boolean

    /**
     * Prepopulate [Engine]s.
     */
    suspend fun prepopulateEngines()

    /**
     * Get all [Engine]s.
     */
    suspend fun getEngines(): List<Engine>

    /**
     * Repair ship.
     */
    suspend fun repairShip(ship: Ship): Ship
}
