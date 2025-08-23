package com.hybris.tlv.usecase.ship

import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship

internal interface ShipUseCases {

    /**
     * Get [Engine]s from the database.
     */
    suspend fun getEngines(): List<Engine>

    /**
     * Upsert a [Ship] into the database.
     */
    suspend fun upsertShip(ship: Ship)

    /**
     * Repair ship.
     */
    suspend fun repairShip(ship: Ship): Ship
}
