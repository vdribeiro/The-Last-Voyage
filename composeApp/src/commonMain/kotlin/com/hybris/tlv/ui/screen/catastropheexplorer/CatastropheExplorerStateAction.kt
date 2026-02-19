package com.hybris.tlv.ui.screen.catastropheexplorer

import com.hybris.tlv.domain.usecase.catastrophe.model.Catastrophe

internal data class CatastropheExplorerState(
    val loading: Boolean = true,
    val catastrophes: List<Catastrophe> = emptyList()
)
