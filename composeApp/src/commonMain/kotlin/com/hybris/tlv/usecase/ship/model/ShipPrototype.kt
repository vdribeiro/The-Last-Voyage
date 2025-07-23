package com.hybris.tlv.usecase.ship.model

internal data class ShipPrototype(
    val assignedPoints: Int,
    val sensorRange: Int,
    val materials: Int,
    val fuel: Int,
    val cryopods: Int,
)
