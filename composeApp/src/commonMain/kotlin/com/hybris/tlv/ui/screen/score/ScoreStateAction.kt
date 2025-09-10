package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.usecase.gamesession.model.GameSession

internal data class ScoreState(
    val loading: Boolean = true,
    val scores: List<GameSession> = emptyList(),
)

internal sealed interface ScoreAction
