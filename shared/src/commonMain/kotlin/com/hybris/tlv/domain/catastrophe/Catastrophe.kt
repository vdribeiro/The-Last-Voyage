package com.hybris.tlv.domain.catastrophe

import kotlinx.serialization.Serializable

@Serializable
data class Catastrophe(
    val id: String,
    val description: String,
)