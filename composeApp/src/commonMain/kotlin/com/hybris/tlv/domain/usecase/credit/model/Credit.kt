package com.hybris.tlv.domain.usecase.credit.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Credit(
    val id: String,
    val link: String?,
    val type: CreditType
)
