package com.hybris.tlv.usecase.event.mapper

import com.hybris.tlv.database.EventSchema
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.space.model.TravelOutcome

internal fun Event.toEventSchema(): EventSchema =
    EventSchema(
        id = id,
        description = description,
        parentId = parentId,
        outcome = outcome?.let { json.encodeToString(value = it) }
    )

internal fun EventSchema.toEvent(): Event =
    Event(
        id = id,
        description = description,
        parentId = parentId,
        outcome = outcome?.let { json.decodeFromString<TravelOutcome>(string = it) }
    )
