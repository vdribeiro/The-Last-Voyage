package com.hybris.tlv.screen.score

import com.hybris.tlv.usecase.gamesession.model.GameSession

internal data class ScoreState(
    val loading: Boolean = true,
    val gameSessions: List<GameSession> = emptyList()
)
