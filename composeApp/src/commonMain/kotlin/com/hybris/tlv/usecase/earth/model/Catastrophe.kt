package com.hybris.tlv.usecase.earth.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Catastrophe(
    val id: String,
    val name: String,
    val description: String,
)