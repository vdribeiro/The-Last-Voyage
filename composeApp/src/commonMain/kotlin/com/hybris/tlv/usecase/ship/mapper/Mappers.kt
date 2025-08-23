package com.hybris.tlv.usecase.ship.mapper

import com.hybris.tlv.database.EngineSchema
import com.hybris.tlv.database.ShipSchema
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.ship.model.ShipPrototype

internal fun ShipPrototype.toShip(id: String = generateUuid()): Ship =
    Ship(
        id = id,
        assignedPoints = assignedPoints,
        yearsTraveled = 0.0,
        sensorRange = sensorRange,
        integrity = 100,
        fuel = fuel,
        materials = materials,
        cryopods = cryopods,
    )

internal fun Engine.toEngineSchema(): EngineSchema =
    EngineSchema(
        id = id,
        name = name,
        description = description,
        velocity = velocity,
    )

internal fun Ship.toShipSchema(): ShipSchema =
    ShipSchema(
        id = id,
        assignedPoints = assignedPoints,
        yearsTraveled = yearsTraveled,
        sensorRange = sensorRange,
        integrity = integrity,
        fuel = fuel,
        materials = materials,
        cryopods = cryopods,
    )

internal fun EngineSchema.toEngine(): Engine =
    Engine(
        id = id,
        name = name,
        description = description,
        velocity = velocity,
    )

internal fun ShipSchema.toShip(): Ship =
    Ship(
        id = id,
        assignedPoints = assignedPoints,
        yearsTraveled = yearsTraveled,
        sensorRange = sensorRange,
        integrity = integrity,
        fuel = fuel,
        materials = materials,
        cryopods = cryopods,
    )
