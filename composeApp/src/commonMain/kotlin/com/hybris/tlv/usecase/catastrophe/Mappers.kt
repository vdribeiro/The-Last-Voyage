package com.hybris.tlv.usecase.catastrophe

import com.hybris.tlv.data.database.CatastropheSchema
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe

internal fun Catastrophe.toCatastropheSchema(): CatastropheSchema =
    CatastropheSchema(
        id = id,
        description = description,
    )

internal fun CatastropheSchema.toCatastrophe(): Catastrophe =
    Catastrophe(
        id = id,
        description = description,
    )
