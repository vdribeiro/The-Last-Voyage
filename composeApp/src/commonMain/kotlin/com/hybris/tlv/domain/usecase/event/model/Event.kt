package com.hybris.tlv.domain.usecase.event.model

import kotlinx.serialization.Serializable
import com.hybris.tlv.domain.usecase.space.model.TravelOutcome

@Serializable
internal data class Event(
    val id: String,
    val description: String,
    val parentId: String?,
    val outcome: TravelOutcome?,
)
