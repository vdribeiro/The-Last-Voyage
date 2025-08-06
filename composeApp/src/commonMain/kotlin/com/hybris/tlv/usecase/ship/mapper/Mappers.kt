package com.hybris.tlv.usecase.ship.mapper

import com.hybris.tlv.database.EngineSchema
import com.hybris.tlv.http.getDouble
import com.hybris.tlv.http.getString
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.remote.ShipApi.Companion.ENGINE_DESCRIPTION
import com.hybris.tlv.usecase.ship.remote.ShipApi.Companion.ENGINE_ID
import com.hybris.tlv.usecase.ship.remote.ShipApi.Companion.ENGINE_NAME
import com.hybris.tlv.usecase.ship.remote.ShipApi.Companion.ENGINE_VELOCITY

internal fun Engine.toEngineMap(): Map<String, Any> =
    buildMap {
        put(key = ENGINE_ID, value = id)
        put(key = ENGINE_NAME, value = name)
        put(key = ENGINE_DESCRIPTION, value = description)
        put(key = ENGINE_VELOCITY, value = velocity)
    }

internal fun Map<String, Any>.toEngine(): Engine =
    Engine(
        id = getString(key = ENGINE_ID)!!,
        name = getString(key = ENGINE_NAME)!!,
        description = getString(key = ENGINE_DESCRIPTION)!!,
        velocity = getDouble(key = ENGINE_VELOCITY)!!,
    )

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
