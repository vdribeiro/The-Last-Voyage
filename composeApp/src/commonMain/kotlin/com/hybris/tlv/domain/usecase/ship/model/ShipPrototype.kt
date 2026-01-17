package com.hybris.tlv.domain.usecase.ship.model

internal data class ShipPrototype(
    val assignedPoints: Int,
    val sensorRange: Int,
    val fuel: Int,
    val materials: Int,
    val cryopods: Int,
)
