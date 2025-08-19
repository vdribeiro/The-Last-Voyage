package com.hybris.tlv.usecase.event

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.event.model.Event
import kotlinx.coroutines.flow.Flow

internal interface EventInternalUseCases {

    /**
     * Rewrites the local and remote [Event] data.
     */
    suspend fun rewriteEvents(): Flow<SyncResult>

    /**
     * Syncs the remote [Event] data to local.
     */
    suspend fun syncEvents(): Flow<SyncResult>

    /**
     * Prepopulate local [Event].
     */
    suspend fun prepopulateEvents()
}
