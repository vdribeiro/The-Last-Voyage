package com.hybris.tlv.ui.screen.tutorial

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store

internal class TutorialStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: TutorialStateBuilder,
): Store<TutorialState, TutorialAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        is TutorialStateBuilder.NewGame -> TutorialState()
    }
) {
    @get:VisibleForTesting
    internal var newGame: Boolean = false

    init {
        when (stateBuilder) {
            is TutorialStateBuilder.NewGame -> newGame = stateBuilder.newGame
        }
    }

    override fun getSavableState(state: TutorialState): Any? =
        TutorialStateBuilder.NewGame(newGame = newGame)

    override fun reducer(state: TutorialState, action: TutorialAction) {
        when (action) {
            TutorialAction.Next -> when (state.tutorialStep) {
                Tutorial.GOAL -> updateState { it.copy(tutorialStep = Tutorial.SHIP) }
                Tutorial.SHIP -> updateState { it.copy(tutorialStep = Tutorial.SYSTEM) }
                Tutorial.SYSTEM -> updateState { it.copy(tutorialStep = Tutorial.TRAVEL) }
                Tutorial.TRAVEL -> updateState { it.copy(tutorialStep = Tutorial.GAME_OVER) }
                Tutorial.GAME_OVER -> when {
                    newGame -> navigate(screen = Screen.NewGame)
                    else -> goBack(state = state)
                }
            }
        }
    }
}
