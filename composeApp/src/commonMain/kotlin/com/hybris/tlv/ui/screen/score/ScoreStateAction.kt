package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface ScoreStateBuilder {
    data object Default: ScoreStateBuilder
    data class FromSavableState(val state: ScoreState): ScoreStateBuilder
}

internal data class ScoreState(
    val loading: Boolean = true,
    val gameSessions: List<GameSession> = emptyList()
)
