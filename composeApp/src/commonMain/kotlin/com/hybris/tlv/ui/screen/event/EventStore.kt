package com.hybris.tlv.ui.screen.event

import kotlinx.coroutines.Job
import androidx.annotation.VisibleForTesting
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Ship

internal class EventStore(
    ship: Ship?,
    private val eventUseCases: EventUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<EventState, EventAction>(
    initialState = EventState(ship = ship)
) {
    @get:VisibleForTesting
    internal var gameSession: GameSession? = null
    @get:VisibleForTesting
    internal var eventChain: List<Event> = emptyList()

    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing game session on setup()"))
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Get event chain and guarantee at least 1 event")
        val eventChain = eventUseCases.getRandomEvent(ids = gameSession.launchedEvents).ifEmpty {
            listOf(defaultEvent)
        }

        val parentEvent = eventChain.find { it.parentId == null }
        if (parentEvent == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing parent event on setup()"))
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Get children events and guarantee at least 1 event")
        val childrenEvents = eventChain.filter { it.parentId == parentEvent.id }.ifEmpty {
            listOf(stopEvent)
        }

        Telemetry.info(tag = TAG, message = "Launch event: $parentEvent")
        val updatedGameSession = gameSessionUseCases.launchEvent(gameSession = gameSession, event = parentEvent)

        this@EventStore.gameSession = updatedGameSession
        this@EventStore.eventChain = eventChain
        updateState {
            it.copy(
                loading = false,
                ship = updatedGameSession.ship,
                parentEvent = parentEvent,
                childrenEvents = childrenEvents,
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    private fun select(action: EventAction.Select): Job = launch(id = "select") {
        Telemetry.info(tag = TAG, message = "Selected event ${action.event}")
        val gameSession = this@EventStore.gameSession
        if (gameSession == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing game session on select()"))
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Check if event chain has ended")
        if (action.event == stopEvent) {
            navigate(screen = Screen.Game(ship = gameSession.ship))
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Continue event chain")
        val childrenEvents = this@EventStore.eventChain.filter { it.parentId == action.event.id }.ifEmpty {
            listOf(stopEvent)
        }

        Telemetry.info(tag = TAG, message = "Launch event: ${action.event}")
        val updatedGameSession = gameSessionUseCases.launchEvent(gameSession = gameSession, event = action.event)

        this@EventStore.gameSession = updatedGameSession
        updateState {
            it.copy(
                ship = updatedGameSession.ship,
                parentEvent = action.event,
                childrenEvents = childrenEvents
            )
        }
    }

    override fun back(state: EventState) {}

    override fun reducer(state: EventState, action: EventAction) {
        when (action) {
            is EventAction.Select -> select(action = action)
        }
    }

    companion object {
        private const val TAG = "EventStore"
    }
}
