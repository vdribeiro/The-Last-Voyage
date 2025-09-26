package com.hybris.tlv.ui.screen.tutorial

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuStateBuilder
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.screen.mainmenu.Content as MainMenuContent

internal class TutorialStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager?,
    audioPlayer: AudioPlayer?
): Store<TutorialState, TutorialAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = TutorialState(
        tutorialStep = Tutorial.GOAL
    )
) {

    override fun back(state: TutorialState): () -> Unit = {
        navigate(
            screen = Screen.MAIN_MENU,
            stateBuilder = MainMenuStateBuilder(currentContent = MainMenuContent.LEARN_MENU)
        )
    }

    override fun reducer(state: TutorialState, action: TutorialAction) {
        when (action) {
            TutorialAction.Next -> when (state.tutorialStep) {
                Tutorial.GOAL -> updateState { it.copy(tutorialStep = Tutorial.SHIP) }
                Tutorial.SHIP -> updateState { it.copy(tutorialStep = Tutorial.SYSTEM) }
                Tutorial.SYSTEM -> updateState { it.copy(tutorialStep = Tutorial.TRAVEL) }
                Tutorial.TRAVEL -> updateState { it.copy(tutorialStep = Tutorial.GAME_OVER) }
                Tutorial.GAME_OVER -> back(state = state).invoke()
            }
        }
    }
}
