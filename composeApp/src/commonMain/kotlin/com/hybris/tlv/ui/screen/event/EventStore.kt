package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import kotlinx.coroutines.Job

internal class EventStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: EventState,
    private val eventUseCases: EventUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<EventAction, EventState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    override fun setup(state: EventState): Job = launch {
        val gameSession = state.gameSession ?: gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing game session on setup()"))
            return@launch
        }

        // Guarantee at least 1 event
        val events = state.events ?: eventUseCases.getRandomEvent(ids = gameSession.launchedEvents).ifEmpty {
            listOf(
                Event(
                    id = "event__default",
                    description = "event__default_description",
                    parentId = null,
                    outcome = null,
                )
            )
        }

        // There must be at least 1 event with no parentId, this is the parent event
        val parentEvent = state.event ?: events.find { it.parentId == null }
        if (parentEvent == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing parent event on setup()"))
            return@launch
        }
        val childrenEvents = state.children ?: events.filter { it.parentId == parentEvent.id }
        val updatedGameSession = gameSessionUseCases.launchEvent(gameSession = gameSession, event = parentEvent)

        updateState {
            it.copy(
                gameSession = updatedGameSession,
                events = events,
                event = parentEvent,
                children = childrenEvents
            )
        }
    }

    override fun back(state: EventState): () -> Unit = {
        navigate(screen = Screen.GAME)
    }

    override fun reducer(state: EventState, action: EventAction) {
        when (action) {
            is EventAction.Select -> select(state = state, action = action)
        }
    }

    private fun select(state: EventState, action: EventAction.Select): Job = launch {
        // Event chain has ended
        if (action.event == null) {
            navigate(screen = Screen.GAME)
            return@launch
        }

        if (state.gameSession == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing game session on select()"))
            return@launch
        }

        // Continue event chain
        val children = state.events?.filter { it.parentId == action.event.id }
        val updatedGameSession = gameSessionUseCases.launchEvent(gameSession = state.gameSession, event = action.event)

        updateState {
            it.copy(
                gameSession = updatedGameSession,
                event = action.event,
                children = children
            )
        }
    }

    companion object {
        private const val TAG = "EventStore"
    }
}
