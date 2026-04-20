package com.hybris.tlv.domain.event

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String,
    val description: String,
    val parentId: String?,
    val outcome: TravelOutcome?,
)