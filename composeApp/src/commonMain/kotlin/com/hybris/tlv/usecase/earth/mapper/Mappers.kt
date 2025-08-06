package com.hybris.tlv.usecase.earth.mapper

import com.hybris.tlv.database.CatastropheSchema
import com.hybris.tlv.http.getString
import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.earth.remote.EarthApi.Companion.CATASTROPHE_DESCRIPTION
import com.hybris.tlv.usecase.earth.remote.EarthApi.Companion.CATASTROPHE_ID
import com.hybris.tlv.usecase.earth.remote.EarthApi.Companion.CATASTROPHE_NAME

internal fun Catastrophe.toCatastropheMap(): Map<String, Any> = buildMap {
    put(key = CATASTROPHE_ID, value = id)
    put(key = CATASTROPHE_NAME, value = name)
    put(key = CATASTROPHE_DESCRIPTION, value = description)
}

internal fun Map<String, Any>.toCatastrophe(): Catastrophe =
    Catastrophe(
        id = getString(key = CATASTROPHE_ID)!!,
        name = getString(key = CATASTROPHE_NAME)!!,
        description = getString(key = CATASTROPHE_DESCRIPTION)!!,
    )

internal fun Catastrophe.toCatastropheSchema(): CatastropheSchema =
    CatastropheSchema(
        id = id,
        name = name,
        description = description,
    )

internal fun CatastropheSchema.toCatastrophe(): Catastrophe =
    Catastrophe(
        id = id,
        name = name,
        description = description,
    )
