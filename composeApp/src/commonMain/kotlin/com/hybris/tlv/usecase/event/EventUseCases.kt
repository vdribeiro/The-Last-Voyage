package com.hybris.tlv.usecase.event

import com.hybris.tlv.usecase.event.model.Event

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
}
