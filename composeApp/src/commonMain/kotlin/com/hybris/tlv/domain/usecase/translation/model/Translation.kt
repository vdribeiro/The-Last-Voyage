package com.hybris.tlv.domain.usecase.translation.model

import kotlinx.serialization.Serializable
import com.hybris.tlv.core.locale.getLanguage

@Serializable
data class Translation(
    val languageIso: String = getLanguage(),
    val key: String,
    val value: String
)