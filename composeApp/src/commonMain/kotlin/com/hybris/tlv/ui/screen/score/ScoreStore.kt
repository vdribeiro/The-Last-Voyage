package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import kotlinx.coroutines.Job

internal class ScoreStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager?,
    audioPlayer: AudioPlayer?,
    private val gameSessionUseCases: GameSessionUseCases
): Store<ScoreState, ScoreAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = ScoreState(
        loading = true,
        gameSessions = emptyList()
    )
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        val gameSessions = gameSessionUseCases.getGameSessions()
            .filter { it.score != null }
            .map { it.copy(utc = getLocalDateTime(utc = it.utc)) }

        updateState {
            it.copy(
                loading = false,
                gameSessions = gameSessions
            )
        }
    }

    override fun back(state: ScoreState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }
}
