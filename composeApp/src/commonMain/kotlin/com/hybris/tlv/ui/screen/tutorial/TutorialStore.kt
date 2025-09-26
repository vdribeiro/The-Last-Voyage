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
    navigation: NavigationManager,
    audioPlayer: AudioPlayer?,
    state: TutorialState?,
    private val stateBuilder: TutorialStateBuilder,
): Store<TutorialState, TutorialAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = state ?: TutorialState(
        tutorialStep = Tutorial.GOAL
    )
) {

    override fun back(state: TutorialState): () -> Unit = {
        if (!stateBuilder.newGame) {
            navigate(screen = Screen.MAIN_MENU, state = MainMenuStateBuilder(currentContent = MainMenuContent.LEARN_MENU))
        } else navigate(screen = Screen.NEW_GAME)
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
