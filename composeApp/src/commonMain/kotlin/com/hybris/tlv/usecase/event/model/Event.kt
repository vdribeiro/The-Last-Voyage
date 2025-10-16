package com.hybris.tlv.usecase.event.model

import kotlinx.serialization.Serializable
import com.hybris.tlv.usecase.space.model.TravelOutcome

@Serializable
internal data class Event(
    val id: String,
    val description: String,
    val parentId: String?,
    val outcome: TravelOutcome?,
)
