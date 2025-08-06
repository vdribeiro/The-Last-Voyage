package com.hybris.tlv.usecase.credits.mapper

import com.hybris.tlv.database.CreditsSchema
import com.hybris.tlv.http.getString
import com.hybris.tlv.usecase.credits.model.Credits
import com.hybris.tlv.usecase.credits.model.CreditsType
import com.hybris.tlv.usecase.credits.remote.CreditsApi.Companion.CREDITS_ID
import com.hybris.tlv.usecase.credits.remote.CreditsApi.Companion.CREDITS_LINK
import com.hybris.tlv.usecase.credits.remote.CreditsApi.Companion.CREDITS_TYPE

internal fun Credits.toCreditsMap(): Map<String, Any> =
    buildMap {
        put(key = CREDITS_ID, value = id)
        link?.let { put(key = CREDITS_LINK, value = it) }
        put(key = CREDITS_TYPE, value = type.name.lowercase())
    }

internal fun Map<String, Any>.toCredits(): Credits =
    Credits(
        id = getString(key = CREDITS_ID)!!,
        link = getString(key = CREDITS_LINK),
        type = CreditsType.valueOf(value = getString(key = CREDITS_TYPE)!!.uppercase())
    )

internal fun Credits.toCreditsSchema(): CreditsSchema =
    CreditsSchema(
        id = id,
        link = link,
        type = type
    )

internal fun CreditsSchema.toCredits(): Credits =
    Credits(
        id = id,
        link = link,
        type = type
    )