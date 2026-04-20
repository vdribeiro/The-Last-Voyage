package com.hybris.tlv.domain.usecase.ship.model

import kotlinx.serialization.Serializable

@Serializable
data class Ship(
    val id: String,
    val engine: Engine,
    val assignedPoints: Int,
    val yearsTraveled: Double,
    val sensorRange: Int,
    val integrity: Int,
    val fuel: Int,
    val materials: Int,
    val cryopods: Int
) {
    companion object {
        const val MAX_INTEGRITY = 100
        const val MAX_SENSOR_RANGE = 10
        const val MIN_SENSOR_RANGE = 1
        const val MAX_FUEL = 5000
        const val MIN_FUEL = 100
        const val MAX_MATERIALS = 2000
        const val MIN_MATERIALS = 100
        const val MAX_CRYOPODS = 2000
        const val MIN_CRYOPODS = 100
    }
}
