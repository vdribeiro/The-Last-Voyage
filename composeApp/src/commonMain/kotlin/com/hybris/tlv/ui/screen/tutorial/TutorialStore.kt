package com.hybris.tlv.ui.screen.tutorial

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.ui.navigation.NewGameScreen
import com.hybris.tlv.ui.store.Store

internal class TutorialStore(
    stateBuilder: TutorialStateBuilder,
): Store<TutorialState, TutorialAction>(
    initialState = TutorialState()
) {
    @get:VisibleForTesting
    internal var newGame: Boolean = false

    init {
        newGame = when (stateBuilder) {
            is TutorialStateBuilder.Default -> stateBuilder.newGame
        }
    }

    override fun reducer(state: TutorialState, action: TutorialAction) {
        when (action) {
            TutorialAction.Next -> when (state.tutorialStep) {
                Tutorial.GOAL -> updateState { it.copy(tutorialStep = Tutorial.SHIP) }
                Tutorial.SHIP -> updateState { it.copy(tutorialStep = Tutorial.SYSTEM) }
                Tutorial.SYSTEM -> updateState { it.copy(tutorialStep = Tutorial.TRAVEL) }
                Tutorial.TRAVEL -> updateState { it.copy(tutorialStep = Tutorial.GAME_OVER) }
                Tutorial.GAME_OVER -> when {
                    newGame -> navigate(screen = NewGameScreen)
                    else -> back(state = state)
                }
            }
        }
    }
}
