package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.usecase.credit.model.Credit

internal sealed interface CreditStateBuilder {
    data object Default: CreditStateBuilder
    data class FromState(val state: CreditState): CreditStateBuilder
}

internal data class CreditState(
    val loading: Boolean = true,
    val credits: List<Credit> = emptyList()
)
