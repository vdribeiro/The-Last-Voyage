package com.hybris.tlv.usecase.ship.model

internal data class Ship(
    val id: String,
    val engine: Engine,
    val assignedPoints: Int,
    val yearsTraveled: Double,
    val sensorRange: Int,
    val integrity: Int,
    val fuel: Int,
    val materials: Int,
    val cryopods: Int
)
