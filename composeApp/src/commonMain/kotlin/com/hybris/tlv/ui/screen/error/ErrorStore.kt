package com.hybris.tlv.ui.screen.error

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store

internal sealed interface ErrorAction {
    data object Back: ErrorAction
    data class SendFeedback(val message: String): ErrorAction
}

internal class ErrorState()

internal class ErrorStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: ErrorState,
): Store<ErrorAction, ErrorState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun reducer(state: ErrorState, action: ErrorAction) {
        when (action) {
            ErrorAction.Back -> navigate(screen = Navigation.Screen.MAIN_MENU)
            is ErrorAction.SendFeedback -> Logger.error(message = action.message) // TODO: Send feedback to server
        }
    }
}
