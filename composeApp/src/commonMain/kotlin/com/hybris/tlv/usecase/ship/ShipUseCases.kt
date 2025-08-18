package com.hybris.tlv.usecase.ship

import com.hybris.tlv.usecase.ship.model.Engine

internal interface ShipUseCases {

    /**
     * Get [Engine]s from the database.
     */
    suspend fun getEngines(): List<Engine>
}
