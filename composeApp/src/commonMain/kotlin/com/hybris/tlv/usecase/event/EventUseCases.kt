package com.hybris.tlv.usecase.event

import com.hybris.tlv.usecase.event.model.Event

internal interface EventUseCases {

    /**
     * Sync [Event]s.
     */
    suspend fun syncEvents()

    /**
     * Prepopulate [Event]s.
     */
    suspend fun prepopulateEvents()

    /**
     * Get a random [Event] and its children given an exclusion list of [ids].
     */
    suspend fun getRandomEvent(ids: Set<String>): List<Event>
}
