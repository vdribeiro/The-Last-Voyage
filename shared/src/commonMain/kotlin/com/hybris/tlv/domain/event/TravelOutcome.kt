package com.hybris.tlv.domain.event

import kotlinx.serialization.Serializable

@Serializable
data class TravelOutcome(
    val integrity: Int? = null,
    val materials: Int? = null,
    val fuel: Int? = null,
    val cryopods: Int? = null,
)