package com.hybris.tlv.usecase.ship

import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.sync.model.SyncResult

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
