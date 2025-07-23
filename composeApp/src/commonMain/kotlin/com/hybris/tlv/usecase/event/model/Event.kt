package com.hybris.tlv.usecase.event.model

import com.hybris.tlv.usecase.space.model.TravelOutcome
import kotlinx.serialization.Serializable

@Serializable
internal data class Event(
    val id: String,
    val name: String,
    val description: String,
    val parentId: String?,
    val outcome: TravelOutcome?,
)
