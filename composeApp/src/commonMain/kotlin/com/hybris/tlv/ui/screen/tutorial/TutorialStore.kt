package com.hybris.tlv.ui.screen.tutorial

import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store

internal class TutorialStore(
    private val newGame: Boolean,
): Store<TutorialState, TutorialAction>(
    initialState = TutorialState()
) {
    override fun reducer(state: TutorialState, action: TutorialAction) {
        when (action) {
            TutorialAction.Next -> when (state.currentContent) {
                Content.GOAL -> updateState { it.copy(currentContent = Content.SHIP) }
                Content.SHIP -> updateState { it.copy(currentContent = Content.SYSTEM) }
                Content.SYSTEM -> updateState { it.copy(currentContent = Content.TRAVEL) }
                Content.TRAVEL -> updateState { it.copy(currentContent = Content.GAME_OVER) }
                Content.GAME_OVER -> when {
                    newGame -> navigate(screen = Screen.NewGame)
                    else -> back(state = state)
                }
            }
        }
    }
}
