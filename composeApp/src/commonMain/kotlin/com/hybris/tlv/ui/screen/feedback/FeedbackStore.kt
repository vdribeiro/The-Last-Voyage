package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import kotlinx.coroutines.Job
import com.hybris.tlv.ui.screen.mainmenu.Content as MainMenuContent

internal sealed interface FeedbackAction {
    data class SendFeedback(val message: String): FeedbackAction
}

internal data class FeedbackState(
    val screen: Screen? = null,
    val throwable: Throwable? = null,
    val identifier: String? = null
)

internal class FeedbackStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: FeedbackState,
): Store<FeedbackAction, FeedbackState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun setBackNavigation(): () -> Unit = {
        sendFeedback(state = stateFlow.value, message = "")
    }

    override fun reducer(state: FeedbackState, action: FeedbackAction) {
        when (action) {
            is FeedbackAction.SendFeedback -> sendFeedback(state = state, message = action.message)
        }
    }

    // TODO: Send feedback to server
    private fun sendFeedback(state: FeedbackState, message: String): Job = launch {
        if (message.isNotBlank()) {
            val feedback = buildList {
                state.screen?.let { add(element = "Screen: $it") }
                state.identifier?.let { add(element = "Identifier: $it") }
                add(element = "Message: $message")
                state.throwable?.let { add(element = "Throwable: $it") }
            }.joinToString(separator = "\n")
            Logger.info(message = feedback)
        }

        when (stateFlow.value.identifier) {
            MainMenuContent.LEARN_MENU.name -> navigate(
                screen = Screen.MAIN_MENU, state = MainMenuState(
                    currentContent = MainMenuContent.LEARN_MENU
                )
            )

            else -> navigate(screen = Screen.MAIN_MENU)
        }
    }
}
