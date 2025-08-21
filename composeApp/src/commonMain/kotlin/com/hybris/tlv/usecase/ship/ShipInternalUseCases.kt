package com.hybris.tlv.usecase.ship

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.ship.model.Engine

internal interface ShipInternalUseCases {

    /**
     * Syncs the remote [Engine] data to local.
     */
    suspend fun syncEngines(): SyncResult

    /**
     * Prepopulate local [Engine].
     */
    suspend fun prepopulateEngines()
}
