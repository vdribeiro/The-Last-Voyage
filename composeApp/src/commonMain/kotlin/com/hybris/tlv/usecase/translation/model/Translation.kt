package com.hybris.tlv.usecase.translation.model

import com.hybris.tlv.usecase.translation.TranslationCache
import kotlinx.serialization.Serializable

@Serializable
internal data class Translation(
    val languageIso: String = TranslationCache.DEFAULT_LANGUAGE,
    val key: String,
    val value: String
)