package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.getLocalDateTime
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import kotlinx.coroutines.Job

internal class ScoreStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: ScoreState,
    private val gameSessionUseCases: GameSessionUseCases
): Store<ScoreAction, ScoreState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun setup(state: ScoreState): Job = launch {
        val loading = state.loading ?: false
        val gameSessions = state.gameSessions ?: gameSessionUseCases.getGameSessions()
            .filter { it.score != null }
            .map { it.copy(utc = getLocalDateTime(utc = it.utc)) }

        updateState {
            it.copy(
                loading = loading,
                gameSessions = gameSessions
            )
        }
    }

    override fun back(state: ScoreState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: ScoreState, action: ScoreAction) {}
}
