package com.hybris.tlv.ui.screen.credit

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.credit.Credit

internal data class CreditState(
    val loading: Boolean = true,
    val creators: ImmutableList<Credit> = persistentListOf(),
    val sources: ImmutableList<Credit> = persistentListOf(),
    val musics: ImmutableList<Credit> = persistentListOf(),
    val supporters: ImmutableList<Credit> = persistentListOf(),
)
