package com.hybris.tlv.usecase.event

import com.hybris.tlv.usecase.event.model.Event

internal interface EventUseCases {

    /**
     * Get [Event]s from the database.
     */
    suspend fun getEvents(): List<Event>

    /**
     * Get a random [Event] and its children from the database given an exclusion list of [ids].
     */
    suspend fun getRandomEvent(ids: Set<String>): List<Event>
}
