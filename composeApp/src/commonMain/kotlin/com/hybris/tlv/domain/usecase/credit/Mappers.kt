package com.hybris.tlv.domain.usecase.credit

import com.hybris.tlv.data.database.CreditSchema
import com.hybris.tlv.domain.usecase.credit.model.Credit

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
