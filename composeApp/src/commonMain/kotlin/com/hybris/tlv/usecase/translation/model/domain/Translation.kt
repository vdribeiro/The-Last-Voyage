package com.hybris.tlv.usecase.translation.model.domain

import kotlinx.serialization.Serializable

@Serializable
internal data class Translation(
    val languageIso: String,
    val key: String,
    val value: String
)
