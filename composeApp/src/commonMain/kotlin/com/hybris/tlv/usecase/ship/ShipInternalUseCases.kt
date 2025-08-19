package com.hybris.tlv.usecase.ship

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.ship.model.Engine
import kotlinx.coroutines.flow.Flow

internal interface ShipInternalUseCases {

    /**
     * Rewrites the local and remote [Engine] data.
     */
    suspend fun rewriteEngines(): Flow<SyncResult>

    /**
     * Syncs the remote [Engine] data to local.
     */
    suspend fun syncEngines(): Flow<SyncResult>

    /**
     * Prepopulate local [Engine].
     */
    suspend fun prepopulateEngines()
}
