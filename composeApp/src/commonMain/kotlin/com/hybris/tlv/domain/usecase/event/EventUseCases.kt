package com.hybris.tlv.domain.usecase.event

import kotlinx.coroutines.flow.Flow
import com.hybris.tlv.domain.event.Event

internal interface EventUseCases {

    /**
     * Sync [Event]s.
     */
    suspend fun syncEvents(): Boolean

    /**
     * Prepopulate [Event]s.
     */
    suspend fun prepopulateEvents(): Boolean

    /**
     * Get a random [Event] and its children given an exclusion list of [ids].
     */
    suspend fun getRandomEvent(ids: Set<String>): List<Event>

    /**
     * Observe all [Event]s.
     */
    fun observeEvents(): Flow<List<Event>>
}
