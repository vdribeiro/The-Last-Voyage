package com.hybris.tlv.usecase.event

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.event.model.Event
import kotlinx.coroutines.flow.Flow

internal interface EventUseCases {

    /**
     * Rewrites the local and remote [Event] data.
     */
    suspend fun setup(): Flow<SyncResult>

    /**
     * Syncs the remote [Event] data to local.
     */
    suspend fun syncEvents(): Flow<SyncResult>

    /**
     * Prepopulate local [Event].
     */
    suspend fun prepopulateEvents()

    /**
     * Get [Event]s from the database.
     */
    suspend fun getEvents(): List<Event>

    /**
     * Get a random [Event] and its children from the database given an exclusion list of [ids].
     */
    suspend fun getRandomEvent(ids: Set<String>): List<Event>
}
