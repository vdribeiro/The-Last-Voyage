package com.hybris.tlv.ui.screen.catastrophe

import com.hybris.tlv.domain.catastrophe.Catastrophe

internal sealed interface CatastropheAction {
    data object Next: CatastropheAction
}

internal data class CatastropheState(
    val loading: Boolean = true,
    val selectedCatastrophe: Catastrophe? = null,
)
