package com.hybris.tlv.ui.screen.catastropheexplorer

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.catastrophe.Catastrophe

internal data class CatastropheExplorerState(
    val loading: Boolean = true,
    val catastrophes: ImmutableList<Catastrophe> = persistentListOf()
)
