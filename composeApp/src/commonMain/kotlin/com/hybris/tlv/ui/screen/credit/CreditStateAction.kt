package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.usecase.credit.model.Credit

internal data class CreditState(
    val credits: List<Credit> = emptyList(),
)

internal sealed interface CreditAction
