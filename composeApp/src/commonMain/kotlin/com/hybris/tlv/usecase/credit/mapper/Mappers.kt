package com.hybris.tlv.usecase.credit.mapper

import com.hybris.tlv.database.CreditSchema
import com.hybris.tlv.http.getString
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.credit.model.CreditType
import com.hybris.tlv.usecase.credit.remote.CreditApi.Companion.CREDITS_ID
import com.hybris.tlv.usecase.credit.remote.CreditApi.Companion.CREDITS_LINK
import com.hybris.tlv.usecase.credit.remote.CreditApi.Companion.CREDITS_TYPE

internal fun Credit.toCreditMap(): Map<String, Any> =
    buildMap {
        put(key = CREDITS_ID, value = id)
        link?.let { put(key = CREDITS_LINK, value = it) }
        put(key = CREDITS_TYPE, value = type.name.lowercase())
    }

internal fun Map<String, Any>.toCredit(): Credit =
    Credit(
        id = getString(key = CREDITS_ID)!!,
        link = getString(key = CREDITS_LINK),
        type = CreditType.valueOf(value = getString(key = CREDITS_TYPE)!!.uppercase())
    )

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