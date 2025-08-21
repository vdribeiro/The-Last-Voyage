package com.hybris.tlv.usecase.earth.mapper

import com.hybris.tlv.database.CatastropheSchema
import com.hybris.tlv.usecase.earth.model.Catastrophe

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
