package com.hybris.tlv.usecase.sync

import com.hybris.tlv.usecase.sync.model.SyncResult

internal interface SyncUseCases {

    /**
     * Resets all local data by deleting configuration and preferences files and clears the entire database.
     */
    suspend fun reset()

    /**
     * Checks if the database is empty.
     */
    suspend fun isEmpty(): Boolean

    /**
     * Sync all data.
     */
    suspend fun sync(
        reset: Boolean,
        progress: (Float) -> Unit = {}
    ): SyncResult
}
