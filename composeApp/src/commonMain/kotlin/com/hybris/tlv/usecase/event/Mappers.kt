package com.hybris.tlv.usecase.event

import com.hybris.tlv.data.database.EventSchema
import com.hybris.tlv.data.serializer.decode
import com.hybris.tlv.data.serializer.encode
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.space.model.TravelOutcome

internal fun Event.toEventSchema(): EventSchema =
    EventSchema(
        id = id,
        description = description,
        parentId = parentId,
        outcome = encode(value = outcome)
    )

internal fun EventSchema.toEvent(): Event =
    Event(
        id = id,
        description = description,
        parentId = parentId,
        outcome = decode<TravelOutcome>(value = outcome)
    )
