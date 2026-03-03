package com.hybris.tlv.ui.screen.score

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hybris.tlv.domain.usecase.gamesession.model.GameSession

internal data class ScoreState(
    val loading: Boolean = true,
    val gameSessions: ImmutableList<GameSession> = persistentListOf()
)
