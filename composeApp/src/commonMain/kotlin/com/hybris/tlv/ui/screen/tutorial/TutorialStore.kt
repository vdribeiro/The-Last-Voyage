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
            TutorialAction.Next -> when (state.tutorialStep) {
                Tutorial.GOAL -> updateState { it.copy(tutorialStep = Tutorial.SHIP) }
                Tutorial.SHIP -> updateState { it.copy(tutorialStep = Tutorial.SYSTEM) }
                Tutorial.SYSTEM -> updateState { it.copy(tutorialStep = Tutorial.TRAVEL) }
                Tutorial.TRAVEL -> updateState { it.copy(tutorialStep = Tutorial.GAME_OVER) }
                Tutorial.GAME_OVER -> when {
                    newGame -> navigate(screen = Screen.NewGame)
                    else -> back(state = state)
                }
            }
        }
    }
}
