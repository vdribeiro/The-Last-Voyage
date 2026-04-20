package com.hybris.tlv.domain.usecase.translation

import com.hybris.tlv.data.database.TranslationSchema
import com.hybris.tlv.domain.translation.Translation

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
