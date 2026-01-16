package com.hybris.tlv.usecase.ship

import com.hybris.tlv.data.database.EngineSchema
import com.hybris.tlv.usecase.ship.model.Engine

internal fun Engine.toEngineSchema(): EngineSchema =
    EngineSchema(
        id = id,
        description = description,
        velocity = velocity,
        fuelConsumption = fuelConsumption,
        cost = cost
    )

internal fun EngineSchema.toEngine(): Engine =
    Engine(
        id = id,
        description = description,
        velocity = velocity,
        fuelConsumption = fuelConsumption,
        cost = cost
    )
