package com.hybris.tlv.usecase.translation.mapper

import com.hybris.tlv.database.TranslationSchema
import com.hybris.tlv.http.getString
import com.hybris.tlv.usecase.translation.model.domain.Translation
import com.hybris.tlv.usecase.translation.remote.TranslationApi.Companion.TRANSLATION_ID
import com.hybris.tlv.usecase.translation.remote.TranslationApi.Companion.TRANSLATION_ISO
import com.hybris.tlv.usecase.translation.remote.TranslationApi.Companion.TRANSLATION_KEY
import com.hybris.tlv.usecase.translation.remote.TranslationApi.Companion.TRANSLATION_VALUE

internal fun Translation.toTranslationMap(): Map<String, Any> =
    buildMap {
        put(key = TRANSLATION_ID, value = "${languageIso}__${key}")
        put(key = TRANSLATION_ISO, value = languageIso)
        put(key = TRANSLATION_KEY, value = key)
        put(key = TRANSLATION_VALUE, value = value)
    }

internal fun Map<String, Any>.toTranslation(): Translation =
    Translation(
        languageIso = getString(key = TRANSLATION_ISO)!!,
        key = getString(key = TRANSLATION_KEY)!!,
        value = getString(key = TRANSLATION_VALUE)!!
    )

internal fun Translation.toTranslationSchema(): TranslationSchema =
    TranslationSchema(
        languageIso = languageIso,
        key = key,
        value_ = value
    )

internal fun TranslationSchema.toTranslation(): Translation =
    Translation(
        languageIso = languageIso,
        key = key,
        value = value_
    )

internal fun List<Translation>.toTranslationCacheMap(): Map<String, String> =
    associate { "${it.languageIso}__${it.key}" to it.value }
