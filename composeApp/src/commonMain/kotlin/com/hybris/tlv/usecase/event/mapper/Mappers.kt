package com.hybris.tlv.usecase.event.mapper

import com.hybris.tlv.database.EventSchema
import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.getString
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.event.remote.EventApi.Companion.EVENT_DESCRIPTION
import com.hybris.tlv.usecase.event.remote.EventApi.Companion.EVENT_ID
import com.hybris.tlv.usecase.event.remote.EventApi.Companion.EVENT_NAME
import com.hybris.tlv.usecase.event.remote.EventApi.Companion.EVENT_OUTCOME
import com.hybris.tlv.usecase.event.remote.EventApi.Companion.EVENT_PARENT_ID
import com.hybris.tlv.usecase.space.model.TravelOutcome

internal fun Event.toEventMap(): Map<String, Any> =
    buildMap {
        put(key = EVENT_ID, value = id)
        put(key = EVENT_NAME, value = name)
        put(key = EVENT_DESCRIPTION, value = description)
        parentId?.let { put(key = EVENT_PARENT_ID, value = it) }
        outcome?.let { put(key = EVENT_OUTCOME, value = json.encodeToString(value = it)) }
    }

internal fun Map<String, Any>.toEvent(): Event =
    Event(
        id = getString(key = EVENT_ID)!!,
        name = getString(key = EVENT_NAME)!!,
        description = getString(key = EVENT_DESCRIPTION)!!,
        parentId = getString(key = EVENT_PARENT_ID),
        outcome = getString(key = EVENT_OUTCOME)?.let { json.decodeFromString<TravelOutcome>(string = it) }
    )

internal fun Event.toEventSchema(): EventSchema =
    EventSchema(
        id = id,
        name = name,
        description = description,
        parentId = parentId,
        outcome = outcome?.let { json.encodeToString(value = it) }
    )

internal fun EventSchema.toEvent(): Event =
    Event(
        id = id,
        name = name,
        description = description,
        parentId = parentId,
        outcome = outcome?.let { json.decodeFromString<TravelOutcome>(string = it) }
    )
