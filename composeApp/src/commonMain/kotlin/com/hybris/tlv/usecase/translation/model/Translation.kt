package com.hybris.tlv.usecase.translation.model

import kotlinx.serialization.Serializable
import com.hybris.tlv.locale.DEFAULT_LANGUAGE

@Serializable
internal data class Translation(
    val languageIso: String = DEFAULT_LANGUAGE,
    val key: String,
    val value: String
)