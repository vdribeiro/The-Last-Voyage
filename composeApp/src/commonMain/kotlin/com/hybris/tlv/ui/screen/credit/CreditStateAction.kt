package com.hybris.tlv.ui.screen.credit

import com.hybris.tlv.usecase.credit.model.Credit

internal data class CreditState(
    val credits: List<Credit>? = null,
)

internal sealed interface CreditAction
