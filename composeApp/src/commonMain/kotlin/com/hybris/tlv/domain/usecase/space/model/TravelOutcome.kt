package com.hybris.tlv.domain.usecase.space.model

import kotlinx.serialization.Serializable

@Serializable
internal data class TravelOutcome(
    val integrity: Int? = null,
    val materials: Int? = null,
    val fuel: Int? = null,
    val cryopods: Int? = null,
)
