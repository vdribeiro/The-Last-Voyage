package com.hybris.tlv.ui.screen.score

import kotlinx.coroutines.Job
import com.hybris.tlv.core.locale.getLocalDateTime
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.domain.usecase.gamesession.GameSessionUseCases

internal class ScoreStore(
    private val gameSessionUseCases: GameSessionUseCases
): Store<ScoreState, Unit>(
    initialState = ScoreState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val gameSessions = gameSessionUseCases.getGameSessions()
            .filter { it.score != null }
            .map { it.copy(utc = getLocalDateTime(utc = it.utc)) }

        updateState {
            it.copy(
                loading = false,
                gameSessions = gameSessions
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    companion object {
        private const val TAG = "ScoreStore"
    }
}
