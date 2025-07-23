package com.hybris.tlv.usecase.ship.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Engine(
    val id: String,
    val name: String,
    val description: String,
    val velocity: Double,
)