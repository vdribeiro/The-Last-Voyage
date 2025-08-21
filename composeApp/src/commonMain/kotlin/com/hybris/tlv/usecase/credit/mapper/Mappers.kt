package com.hybris.tlv.usecase.credit.mapper

import com.hybris.tlv.database.CreditSchema
import com.hybris.tlv.usecase.credit.model.Credit

internal fun Credit.toCreditSchema(): CreditSchema =
    CreditSchema(
        id = id,
        link = link,
        type = type
    )

internal fun CreditSchema.toCredit(): Credit =
    Credit(
        id = id,
        link = link,
        type = type
    )