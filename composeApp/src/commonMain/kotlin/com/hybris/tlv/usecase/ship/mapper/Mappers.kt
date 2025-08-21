package com.hybris.tlv.usecase.ship.mapper

import com.hybris.tlv.database.EngineSchema
import com.hybris.tlv.usecase.ship.model.Engine

internal fun Engine.toEngineSchema(): EngineSchema =
    EngineSchema(
        id = id,
        name = name,
        description = description,
        velocity = velocity,
    )

internal fun EngineSchema.toEngine(): Engine =
    Engine(
        id = id,
        name = name,
        description = description,
        velocity = velocity,
    )
