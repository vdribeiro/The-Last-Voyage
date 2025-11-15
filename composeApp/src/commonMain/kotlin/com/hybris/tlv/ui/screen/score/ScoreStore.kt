package com.hybris.tlv.ui.screen.score

import kotlinx.coroutines.Job
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases

internal class ScoreStore(
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: ScoreStateBuilder,
    private val gameSessionUseCases: GameSessionUseCases
): Store<ScoreState, Unit>(
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        ScoreStateBuilder.Default -> ScoreState()
        is ScoreStateBuilder.FromState -> stateBuilder.state
    }
) {
    init {
        when (stateBuilder) {
            ScoreStateBuilder.Default -> setup()
            is ScoreStateBuilder.FromState -> {}
        }
    }

    override fun getSavableState(state: ScoreState): Any =
        ScoreStateBuilder.FromState(state = state)

    private fun setup(): Job = launch {
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
