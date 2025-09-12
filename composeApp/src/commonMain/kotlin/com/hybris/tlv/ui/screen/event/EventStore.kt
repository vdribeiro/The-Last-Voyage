package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession
import kotlinx.coroutines.Job

internal class EventStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    stateBuilder: EventStateBuilder,
    private val eventUseCases: EventUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<EventAction, EventState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = EventState()
) {
    private val defaultEvent = Event(
        id = "event__default",
        description = "event__default_description",
        parentId = null,
        outcome = null,
    )
    private val stopEvent = Event(
        id = "event__default_continue",
        description = "event__default_continue",
        parentId = null,
        outcome = null,
    )

    private var gameSession: GameSession? = null
    private val events: MutableList<Event> = mutableListOf()

    init {
        setup(builder = stateBuilder)
    }

    override fun setup(state: EventState): Job = launch {}

    private fun setup(builder: EventStateBuilder): Job = launch {
        val gameSession = builder.gameSession ?: gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing game session on setup()"))
            return@launch
        }

        // Guarantee at least 1 event
        val events = builder.events ?: eventUseCases.getRandomEvent(ids = gameSession.launchedEvents).ifEmpty {
            listOf(element = defaultEvent)
        }

        // There must be at least 1 event with no parentId, this is the parent event
        val parentEvent = events.find { it.parentId == null }
        if (parentEvent == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing parent event on setup()"))
            return@launch
        }
        val childrenEvents = events.filter { it.parentId == parentEvent.id }.ifEmpty {
            listOf(element = stopEvent)
        }
        val updatedGameSession = gameSessionUseCases.launchEvent(gameSession = gameSession, event = parentEvent)

        this@EventStore.gameSession = updatedGameSession
        this@EventStore.events.addAll(elements = events)
        updateState {
            it.copy(
                ship = updatedGameSession.ship,
                parentEvent = parentEvent,
                childrenEvents = childrenEvents
            )
        }
    }

    override fun back(state: EventState): () -> Unit = {
        navigate(screen = Screen.GAME)
    }

    override fun reducer(state: EventState, action: EventAction) {
        when (action) {
            is EventAction.Select -> select(action = action)
        }
    }

    private fun select(action: EventAction.Select): Job = launch {
        // Event chain has ended
        if (action.event == stopEvent) {
            navigate(screen = Screen.GAME)
            return@launch
        }

        val gameSession = this@EventStore.gameSession
        if (gameSession == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackState(tag = TAG, message = "Invalid state: missing game session on select()"))
            return@launch
        }

        // Continue event chain
        val children = events.filter { it.parentId == action.event.id }
        val updatedGameSession = gameSessionUseCases.launchEvent(gameSession = gameSession, event = action.event)

        updateState {
            it.copy(
                ship = updatedGameSession.ship,
                parentEvent = action.event,
                childrenEvents = children
            )
        }
    }

    companion object {
        private const val TAG = "EventStore"
    }
}
