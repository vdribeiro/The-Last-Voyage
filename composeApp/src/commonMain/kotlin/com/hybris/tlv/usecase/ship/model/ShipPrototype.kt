package com.hybris.tlv.usecase.ship.model

internal data class ShipPrototype(
    val assignedPoints: Int,
    val sensorRange: Int,
    val fuel: Int,
    val materials: Int,
    val cryopods: Int,
)
