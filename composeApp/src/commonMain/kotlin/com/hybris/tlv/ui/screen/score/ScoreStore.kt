package com.hybris.tlv.ui.screen.score

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface ScoreAction

internal data class ScoreState(
    val loading: Boolean = true,
    val scores: List<GameSession> = emptyList(),
)

internal class ScoreStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: ScoreState,
    private val locale: Locale,
    private val gameSessionUseCases: GameSessionUseCases
): Store<ScoreAction, ScoreState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val gameSessions = gameSessionUseCases.getGameSessions()
        val scores = gameSessions
            .filter { it.score != null }
            .map { it.copy(utc = locale.getLocalDateTime(utc = it.utc)) }
        updateState {
            it.copy(
                loading = false,
                scores = scores
            )
        }
    }

    override fun setBackNavigation(state: ScoreState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: ScoreState, action: ScoreAction) {}
}
