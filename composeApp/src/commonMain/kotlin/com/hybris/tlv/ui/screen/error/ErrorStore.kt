package com.hybris.tlv.ui.screen.error

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.mainmenu.Content as MainMenuContent
import com.hybris.tlv.ui.store.Store

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
        when (stateFlow.value.identifier) {
            MainMenuContent.LEARN_MENU.name -> navigate(
                screen = Screen.MAIN_MENU, state = MainMenuState(
                    currentContent = MainMenuContent.LEARN_MENU
                )
            )

            else -> navigate(screen = Screen.MAIN_MENU)
        }
    }

    override fun reducer(state: ErrorState, action: ErrorAction) {
        when (action) {
            is ErrorAction.SendFeedback -> Logger.error(message = action.message) // TODO: Send feedback to server
        }
    }
}
