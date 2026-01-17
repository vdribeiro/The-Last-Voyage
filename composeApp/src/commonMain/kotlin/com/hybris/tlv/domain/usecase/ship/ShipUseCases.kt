package com.hybris.tlv.domain.usecase.ship

import com.hybris.tlv.domain.usecase.ship.model.Engine
import com.hybris.tlv.domain.usecase.ship.model.Ship

internal interface ShipUseCases {

    /**
     * Sync [Engine]s.
     */
    suspend fun syncEngines(): Boolean

    /**
     * Prepopulate [Engine]s.
     */
    suspend fun prepopulateEngines(): Boolean

    /**
     * Get all [Engine]s.
     */
    suspend fun getEngines(): List<Engine>

    /**
     * Repair ship.
     */
    suspend fun repairShip(ship: Ship): Ship
}
