package com.hybris.tlv.usecase.ship.remote

import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.ship.model.Engine

internal interface ShipRemote {

    /**
     * Get engines from the API.
     */
    suspend fun getEngines(): Result<Engine>
}
