package com.hybris.tlv.usecase.sync

import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlinx.coroutines.flow.Flow

internal interface SyncUseCases {

    /**
     * Get exoplanet data from the NASA archive.
     */
    fun getArchive(): Flow<SyncResult>

    /**
     * Syncs all the remote data to local, or prepopulates if it fails
     */
    fun sync(): Flow<SyncResult>
}
