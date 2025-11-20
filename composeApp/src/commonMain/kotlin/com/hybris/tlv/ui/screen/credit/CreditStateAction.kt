package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.usecase.credit.model.Credit

internal data class CreditState(
    val loading: Boolean = true,
    val credits: List<Credit> = emptyList()
)
