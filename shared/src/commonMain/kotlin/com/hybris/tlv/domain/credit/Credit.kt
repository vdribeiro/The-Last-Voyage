package com.hybris.tlv.domain.credit

import kotlinx.serialization.Serializable

@Serializable
data class Credit(
    val id: String,
    val link: String?,
    val type: CreditType
)
