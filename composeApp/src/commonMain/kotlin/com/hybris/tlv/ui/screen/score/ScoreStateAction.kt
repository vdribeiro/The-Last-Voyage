package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.domain.usecase.gamesession.model.GameSession

internal data class ScoreState(
    val loading: Boolean = true,
    val gameSessions: List<GameSession> = emptyList()
)
