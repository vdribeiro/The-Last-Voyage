package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.error.ErrorState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface EventAction {
    data class Select(val event: Event?): EventAction
}

internal data class EventState(
    val gameSession: GameSession? = null,
    val events: List<Event> = emptyList(),
    val event: Event? = null,
    val children: List<Event> = emptyList()
)

internal class EventStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    initialState: EventState?,
    private val eventUseCases: EventUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<EventAction, EventState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState ?: EventState()
) {
    init {
        if (initialState == null) setup()
    }

    private fun setup() = launchInPipeline {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.EVENT,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "EventStore:setup"
                )
            )
            return@launchInPipeline
        }

        // Guarantee at least 1 event
        val events = eventUseCases.getRandomEvent(ids = gameSession.launchedEvents).ifEmpty {
            listOf(
                Event(
                    id = "default",
                    name = "event__default",
                    description = "event__default_description",
                    parentId = null,
                    outcome = null,
                )
            )
        }
        val event = events.find { it.parentId == null }
        if (event == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing parent event")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.EVENT,
                    throwable = IllegalStateException("Invalid state: missing parent event"),
                    identifier = "EventStore:setup"
                )
            )
            return@launchInPipeline
        }

        val children = events.filter { it.parentId == event.id }
        val updatedGameSession = gameSessionUseCases.doEvent(gameSession = gameSession, event = event)

        updateState {
            it.copy(
                gameSession = updatedGameSession,
                events = events,
                event = event,
                children = children
            )
        }
    }

    private fun select(state: EventState, action: EventAction.Select) = launchInPipeline {
        if (state.gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(
                screen = Screen.ERROR, state = ErrorState(
                    screen = Screen.EVENT,
                    throwable = IllegalStateException("Invalid state: missing game session"),
                    identifier = "EventStore:reducer:Select"
                )
            )
            return@launchInPipeline
        }

        if (action.event == null) {
            navigate(screen = Screen.GAME)
            return@launchInPipeline
        }

        val children = state.events.filter { it.parentId == action.event.id }
        val updatedGameSession = gameSessionUseCases.doEvent(gameSession = state.gameSession, event = action.event)

        updateState {
            it.copy(
                gameSession = updatedGameSession,
                event = action.event,
                children = children
            )
        }
    }

    override fun setBackNavigation(state: EventState): () -> Unit = {
        navigate(screen = Screen.MAIN_MENU)
    }

    override fun reducer(state: EventState, action: EventAction) {
        when (action) {
            is EventAction.Select -> select(state = state, action = action)
        }
    }

    companion object {
        private const val TAG = "EventStore"
    }
}
