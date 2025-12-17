package com.hybris.tlv.usecase.sync

internal interface SyncUseCases {

    /**
     * Resets all local data for the application.
     * It deletes configuration and preferences files and clears the entire database.
     */
    suspend fun reset()

    /**
     * Sync all data with remote.
     */
    suspend fun sync(progress: (Float) -> Unit)
}
