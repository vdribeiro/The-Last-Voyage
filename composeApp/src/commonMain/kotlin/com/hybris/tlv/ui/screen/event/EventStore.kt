package com.hybris.tlv.ui.screen.event

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationManager.Screen
import com.hybris.tlv.ui.screen.feedback.FeedbackStateBuilder
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession
import kotlinx.coroutines.Job

internal class EventStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    private val eventUseCases: EventUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<EventAction, EventState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = EventState(
        loading = true,
        ship = null,
        parentEvent = defaultEvent,
        childrenEvents = emptyList()
    )
) {
    @get:VisibleForTesting
    internal var gameSession: GameSession? = null
    @get:VisibleForTesting
    internal val eventChain: MutableList<Event> = mutableListOf()

    init {
        setup()
    }

    private fun setup(): Job = launch {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            navigate(screen = Screen.FEEDBACK, stateBuilder = FeedbackStateBuilder(tag = TAG, message = "Invalid state: missing game session on setup()"))
            return@launch
        }

        // Guarantee at least 1 event
        val eventChain = eventUseCases.getRandomEvent(ids = gameSession.launchedEvents).ifEmpty {
            listOf(element = defaultEvent)
        }

        // There must be at least 1 event with no parentId, this is the parent event
        val parentEvent = eventChain.find { it.parentId == null }
        if (parentEvent == null) {
            navigate(screen = Screen.FEEDBACK, stateBuilder = FeedbackStateBuilder(tag = TAG, message = "Invalid state: missing parent event on setup()"))
            return@launch
        }
        val childrenEvents = eventChain.filter { it.parentId == parentEvent.id }.ifEmpty {
            listOf(element = stopEvent)
        }
        val updatedGameSession = gameSessionUseCases.launchEvent(gameSession = gameSession, event = parentEvent)

        this@EventStore.gameSession = updatedGameSession
        this@EventStore.eventChain.addAll(elements = eventChain)
        updateState {
            it.copy(
                loading = false,
                ship = updatedGameSession.ship,
                parentEvent = parentEvent,
                childrenEvents = childrenEvents
            )
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
            navigate(screen = Screen.FEEDBACK, stateBuilder = FeedbackStateBuilder(tag = TAG, message = "Invalid state: missing game session on select()"))
            return@launch
        }

        // Continue event chain
        val childrenEvents = eventChain.filter { it.parentId == action.event.id }.ifEmpty {
            listOf(element = stopEvent)
        }
        val updatedGameSession = gameSessionUseCases.launchEvent(gameSession = gameSession, event = action.event)

        updateState {
            it.copy(
                ship = updatedGameSession.ship,
                parentEvent = action.event,
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

    companion object {
        private const val TAG = "EventStore"
    }
}
