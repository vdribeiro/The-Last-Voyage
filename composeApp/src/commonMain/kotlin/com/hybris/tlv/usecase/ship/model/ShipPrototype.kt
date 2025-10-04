package com.hybris.tlv.usecase.ship.model

internal data class ShipPrototype(
    val engineId: String,
    val assignedPoints: Int,
    val sensorRange: Int,
    val fuel: Int,
    val materials: Int,
    val cryopods: Int,
)
