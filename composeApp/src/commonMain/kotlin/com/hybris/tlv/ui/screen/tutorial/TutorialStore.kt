package com.hybris.tlv.ui.screen.tutorial

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store

internal class TutorialStore(
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: TutorialStateBuilder,
): Store<TutorialState, TutorialAction>(
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        is TutorialStateBuilder.Default -> TutorialState()
        is TutorialStateBuilder.FromState -> stateBuilder.state
    }
) {
    @get:VisibleForTesting
    internal var newGame: Boolean = false

    init {
        newGame = when (stateBuilder) {
            is TutorialStateBuilder.Default -> stateBuilder.newGame
            is TutorialStateBuilder.FromState -> stateBuilder.newGame
        }
    }

    override fun getSavableState(state: TutorialState): Any =
        TutorialStateBuilder.FromState(state = state, newGame = newGame)

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
