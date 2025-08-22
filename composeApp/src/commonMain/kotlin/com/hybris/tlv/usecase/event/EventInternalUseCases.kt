package com.hybris.tlv.usecase.event

import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.sync.model.SyncResult

internal interface EventInternalUseCases {

    /**
     * Syncs the remote [Event] data to local.
     */
    suspend fun syncEvents(): SyncResult

    /**
     * Prepopulate local [Event].
     */
    suspend fun prepopulateEvents()
}
