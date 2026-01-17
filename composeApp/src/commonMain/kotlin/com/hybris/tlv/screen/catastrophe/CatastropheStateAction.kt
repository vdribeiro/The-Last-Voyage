package com.hybris.tlv.screen.catastrophe

import com.hybris.tlv.usecase.catastrophe.model.Catastrophe

internal sealed interface CatastropheAction {
    data object Next: CatastropheAction
}

internal data class CatastropheState(
    val loading: Boolean = true,
    val selectedCatastrophe: Catastrophe? = null,
)
