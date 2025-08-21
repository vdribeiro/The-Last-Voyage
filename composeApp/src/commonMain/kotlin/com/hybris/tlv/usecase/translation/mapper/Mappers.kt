package com.hybris.tlv.usecase.translation.mapper

import com.hybris.tlv.database.TranslationSchema
import com.hybris.tlv.usecase.translation.model.domain.Translation

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
