package com.hybris.tlv.usecase.event.local

import com.hybris.tlv.usecase.event.model.Event

internal interface EventLocal {

    /**
     * Returns true if there are no [Event]s in the database, false otherwise.
     */
    fun isEventEmpty(): Boolean

    /**
     * Rewrites the [Event] table with the given [events].
     */
    fun rewriteEvents(events: List<Event>)

    /**
     * Get [Event]s from the database.
     */
    fun getEvents(): List<Event>

    /**
     * Get a random [Event] and its children given an exclusion list of [ids].
     */
    fun getRandomEvent(ids: Set<String>): List<Event>
}
