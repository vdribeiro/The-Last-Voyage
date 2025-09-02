package com.hybris.tlv.usecase.learning.model

internal data class Learning(
    val id: String,
    val description: String,
    val image: String?,
    val type: LearningType
)
