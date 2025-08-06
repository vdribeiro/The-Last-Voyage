package com.hybris.tlv.usecase.credits.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Credits(
    val id: String,
    val link: String?,
    val type: CreditsType
)
