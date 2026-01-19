package com.hybris.tlv.domain.usecase.sync

import com.hybris.tlv.domain.usecase.sync.model.SyncResult

internal interface SyncUseCases {

    /**
     * Resets all local data by deleting configuration and preferences files and clears the entire database.
     */
    suspend fun reset()

    /**
     * Sync all data, given an optional [reset] that if true resets all local data before syncing, and a [progress] callback with values from 0.0 to 1.0.
     */
    suspend fun sync(reset: Boolean = false, progress: (Float) -> Unit = {}): SyncResult
}
