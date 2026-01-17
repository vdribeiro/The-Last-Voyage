package com.hybris.tlv.domain.usecase.catastrophe.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Catastrophe(
    val id: String,
    val description: String,
)