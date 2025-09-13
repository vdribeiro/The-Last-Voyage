package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.usecase.credit.model.Credit

internal sealed interface CreditAction

internal data class CreditState(
    val loading: Boolean,
    val credits: List<Credit>
)
