package com.hybris.tlv.usecase.ship.local

import com.hybris.tlv.usecase.ship.model.Engine

internal interface ShipLocal {

    /**
     * Returns true if there are no [Engine]s in the database, false otherwise.
     */
    fun isEngineEmpty(): Boolean

    /**
     * Rewrites the [Engine] table with the given [engines].
     */
    fun rewriteEngines(engines: List<Engine>)

    /**
     * Get [Engine]s from the database.
     */
    fun getEngines(): List<Engine>
}
