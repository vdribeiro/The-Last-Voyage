package com.hybris.tlv.usecase.achievement.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Precondition(
    val settledHostId: String? = null,
    val settledPlanetId: String? = null,
    val integrity: String? = null,
    val yearsTraveled: String? = null,
    val sensorRange: String? = null,
    val materials: String? = null,
    val fuel: String? = null,
    val cryopods: String? = null,
    val habitability: String? = null,
)
