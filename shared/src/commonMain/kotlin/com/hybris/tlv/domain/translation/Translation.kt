package com.hybris.tlv.domain.translation

import kotlinx.serialization.Serializable

@Serializable
data class Translation(
    val languageIso: String,
    val key: String,
    val value: String
)
