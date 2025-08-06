package com.hybris.tlv.usecase.achievement.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Precondition(
    val settledPlanetId: String? = null,
    val shipDestroyed: Boolean? = null,
    val yearsTraveled: Double? = null,
    val sensorRange: Int? = null,
    val materials: Int? = null,
    val fuel: Int? = null,
    val cryopods: Int? = null,
)
