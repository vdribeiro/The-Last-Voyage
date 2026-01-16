package com.hybris.tlv.ui.screen.tutorial

import kotlinx.coroutines.Job
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.screen.Store

internal class TutorialStore(
    private val newGame: Boolean,
    private val config: ConfigManager,
): Store<TutorialState, TutorialAction>(
    initialState = TutorialState()
) {

    private fun finish(state: TutorialState): Job = launch(id = "finish") {
        config.setPreferences { it.copy(showTutorial = false) }
        when {
            newGame -> navigate(screen = Screen.NewGame)
            else -> back(state = state)
        }
    }

    private fun next(state: TutorialState) {
        when (state.currentContent) {
            Content.WELCOME -> updateState { it.copy(currentContent = Content.GOAL) }
            Content.GOAL -> updateState { it.copy(currentContent = Content.SHIP) }
            Content.SHIP -> updateState { it.copy(currentContent = Content.TRAVEL) }
            Content.TRAVEL -> updateState { it.copy(currentContent = Content.SYSTEM) }
            Content.SYSTEM -> updateState { it.copy(currentContent = Content.GAME_OVER) }
            Content.GAME_OVER -> finish(state = state)
        }
    }

    override fun reducer(state: TutorialState, action: TutorialAction) {
        when (action) {
            TutorialAction.Next -> next(state = state)
            TutorialAction.Skip -> finish(state = state)
        }
    }
}
