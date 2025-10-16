package com.hybris.tlv.usecase.translation.model

import kotlinx.serialization.Serializable
import com.hybris.tlv.usecase.translation.TranslationCache

@Serializable
internal data class Translation(
    val languageIso: String = TranslationCache.DEFAULT_LANGUAGE,
    val key: String,
    val value: String
)