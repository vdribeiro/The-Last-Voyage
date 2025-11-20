package com.hybris.tlv.ui.screen.event

import kotlinx.coroutines.Job
import androidx.annotation.VisibleForTesting
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.game.GameStateBuilder
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal class EventStore(
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    stateBuilder: EventStateBuilder,
    private val eventUseCases: EventUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<EventState, EventAction>(
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = when (stateBuilder) {
        EventStateBuilder.Default -> EventState()
        is EventStateBuilder.WithShip -> EventState(ship = stateBuilder.ship)
        is EventStateBuilder.FromState -> stateBuilder.state
    }
) {
    @get:VisibleForTesting
    internal var gameSession: GameSession? = null
    @get:VisibleForTesting
    internal var eventChain: List<Event>? = null

    init {
        when (stateBuilder) {
            EventStateBuilder.Default -> setup()
            is EventStateBuilder.WithShip -> setup()
            is EventStateBuilder.FromState -> {
                gameSession = stateBuilder.gameSession
                eventChain = stateBuilder.eventChain
            }
        }
    }

    override fun getSavableState(state: EventState): Any =
        EventStateBuilder.FromState(state = state, gameSession = gameSession, eventChain = eventChain.orEmpty())

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            error(tag = TAG, message = "Invalid state: missing game session on setup()")
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Get event chain and guarantee at least 1 event")
        val eventChain = eventUseCases.getRandomEvent(ids = gameSession.launchedEvents).ifEmpty {
            listOf(element = defaultEvent)
        }

        val parentEvent = eventChain.find { it.parentId == null }
        if (parentEvent == null) {
            error(tag = TAG, message = "Invalid state: missing parent event on setup()")
            return@launch
        }
        Telemetry.info(tag = TAG, message = "Get children events and guarantee at least 1 event")
        val childrenEvents = eventChain.filter { it.parentId == parentEvent.id }.ifEmpty {
            listOf(element = stopEvent)
        }

        Telemetry.info(tag = TAG, message = "Launch event")
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

    private fun select(action: EventAction.Select): Job = launch {
        Telemetry.info(tag = TAG, message = "Selected event ${action.event}")
        val gameSession = this@EventStore.gameSession
        if (gameSession == null) {
            error(tag = TAG, message = "Invalid state: missing game session on select()")
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Check if event chain has ended")
        if (action.event == stopEvent) {
            navigate(screen = Screen.Game, stateBuilder = GameStateBuilder.WithShip(ship = gameSession.ship))
            return@launch
        }

        Telemetry.info(tag = TAG, message = "Continue event chain")
        val childrenEvents = this@EventStore.eventChain.orEmpty().filter { it.parentId == action.event.id }.ifEmpty {
            listOf(element = stopEvent)
        }
        Telemetry.info(tag = TAG, message = "Launch event")
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
