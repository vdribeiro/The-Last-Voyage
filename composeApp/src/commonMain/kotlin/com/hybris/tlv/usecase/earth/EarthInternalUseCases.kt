package com.hybris.tlv.usecase.earth

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.earth.model.Catastrophe
import kotlinx.coroutines.flow.Flow

internal interface EarthInternalUseCases {

    /**
     * Rewrites the local and remote [Catastrophe] data.
     */
    suspend fun rewrite(): Flow<SyncResult>

    /**
     * Syncs the remote [Catastrophe] data to local.
     */
    suspend fun syncCatastrophes(): Flow<SyncResult>

    /**
     * Prepopulate local [Catastrophe].
     */
    suspend fun prepopulateCatastrophes()
}
