package com.hybris.tlv.ui.screen.feedback

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.store.Store
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

private class FeedbackStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: FeedbackState,
): Store<FeedbackAction, FeedbackState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun setup(state: FeedbackState): Job = launch {
        val tag = state.tag
        val message = state.message
        if (tag != null && message != null) Logger.error(tag = tag, message = message)
        updateState {
            it.copy(
                tag = tag,
                message = message
            )
        }
    }

    override fun back(state: FeedbackState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: FeedbackState, action: FeedbackAction) {
        when (action) {
            is FeedbackAction.SendFeedback -> sendFeedback(state = state, message = action.message)
        }
    }

    private fun sendFeedback(state: FeedbackState, message: String): Job = launch {
        // Construct the feedback message with all the components
        val feedback = buildList {
            state.tag?.let { add(element = "Identifier: $it") }
            state.message?.let { add(element = "Message: $it") }
            if (message.isNotBlank()) add(element = "Feedback: $message")
        }.joinToString(separator = "\n")
        // TODO: Send feedback to server
        println(feedback)
        delay(timeMillis = 2000L)
        navigate(screen = Screen.MAIN_MENU)
    }
}

internal fun createFeedbackStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: FeedbackState,
): Store<FeedbackAction, FeedbackState> = FeedbackStore(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
)
