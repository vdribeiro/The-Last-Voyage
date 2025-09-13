package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface ScoreAction

internal data class ScoreState(
    val loading: Boolean,
    val gameSessions: List<GameSession>,
)
