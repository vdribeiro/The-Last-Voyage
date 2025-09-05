package com.hybris.tlv.ui.screen.error

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import kotlinx.coroutines.Job
import com.hybris.tlv.ui.screen.mainmenu.Content as MainMenuContent

internal sealed interface ErrorAction {
    data class SendFeedback(val message: String): ErrorAction
}

internal data class ErrorState(
    val screen: Screen? = null,
    val throwable: Throwable? = null,
    val identifier: String? = null
)

internal class ErrorStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: ErrorState,
): Store<ErrorAction, ErrorState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun setBackNavigation(): () -> Unit = {
        sendFeedback(state = stateFlow.value, message = "")
    }

    override fun reducer(state: ErrorState, action: ErrorAction) {
        when (action) {
            is ErrorAction.SendFeedback -> sendFeedback(state = state, message = action.message)
        }
    }

    // TODO: Send feedback to server
    private fun sendFeedback(state: ErrorState, message: String): Job = launch {
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
