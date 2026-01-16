package com.hybris.tlv.usecase.translation.model

import kotlinx.serialization.Serializable
import com.hybris.tlv.locale.getLanguage

@Serializable
internal data class Translation(
    val languageIso: String = getLanguage(),
    val key: String,
    val value: String
)