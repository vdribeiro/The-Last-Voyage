package com.hybris.tlv.usecase.learning.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Learning(
    val id: String,
    val description: String,
    val image: String?,
    val type: LearningType
)
