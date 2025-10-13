package com.hybris.tlv.usecase.achievement.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Achievement(
    val id: String,
    val description: String,
    val preconditions: Precondition,
    val done: Boolean = false
)
