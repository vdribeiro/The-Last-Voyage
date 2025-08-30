package com.hybris.tlv.usecase.sync

import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlinx.coroutines.flow.Flow

internal interface SyncUseCases {

    /**
     * Syncs all the remote data to local, or prepopulates if it fails
     */
    suspend fun sync(): Flow<SyncResult>

    /**
     * Get exoplanet data from the NASA archive.
     */
    suspend fun getArchive(): Flow<SyncResult>
}
