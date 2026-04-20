package com.hybris.tlv.domain.achievement

import kotlinx.serialization.Serializable

@Serializable
data class Achievement(
    val id: String,
    val description: String,
    val preconditions: Precondition,
    val done: Boolean = false
)
