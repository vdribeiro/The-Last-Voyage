package com.hybris.tlv.domain.ship

import kotlinx.serialization.Serializable

@Serializable
data class Engine(
    val id: String,
    val description: String,
    val velocity: Double,
    val fuelConsumption: Double,
    val cost: Int
)