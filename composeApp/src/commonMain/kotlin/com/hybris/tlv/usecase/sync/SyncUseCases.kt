package com.hybris.tlv.usecase.sync

internal interface SyncUseCases {

    /**
     * Resets all local data by deleting configuration and preferences files and clears the entire database.
     */
    suspend fun reset()

    /**
     * Sync all data.
     */
    suspend fun sync(progress: (Float) -> Unit)
}
