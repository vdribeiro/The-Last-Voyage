package com.hybris.tlv.usecase.sync

import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlinx.coroutines.flow.Flow

internal interface SyncUseCases {

    /**
     * Warms up the core.
     */
    suspend fun setup()

    /**
     * Get exoplanet data from the NASA archive.
     */
    suspend fun getArchive(): Flow<SyncResult>

    /**
     * Prepopulates all local data.
     */
    suspend fun prepopulate(): Flow<SyncResult>

    /**
     * Syncs all the remote data to local.
     */
    suspend fun sync(): Flow<SyncResult>
}