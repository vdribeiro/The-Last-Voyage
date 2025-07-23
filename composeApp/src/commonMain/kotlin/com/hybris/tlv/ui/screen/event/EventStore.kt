package com.hybris.tlv.ui.screen.event

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession

internal sealed interface EventAction {
    data object Back: EventAction
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
    navigation: Navigation,
    initialState: EventState,
    private val eventUseCases: EventUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<EventAction, EventState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(screen = Navigation.Screen.ERROR)
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
            navigate(screen = Navigation.Screen.ERROR)
            return@launchInPipeline
        }

        val children = events.filter { it.parentId == event.id }
        val updatedGameSession = updateGameSession(
            gameSession = gameSession,
            event = event
        )

        gameSessionUseCases.updateGameSession(gameSession = updatedGameSession)
        updateState {
            it.copy(
                gameSession = updatedGameSession,
                events = events,
                event = event,
                children = children
            )
        }
    }

    private fun updateGameSession(gameSession: GameSession, event: Event): GameSession {
        val integrity = gameSession.integrity + (event.outcome?.integrity ?: 0)
        val materials = gameSession.materials + (event.outcome?.materials ?: 0)
        val fuel = gameSession.fuel + (event.outcome?.fuel ?: 0)
        val cryopods = gameSession.cryopods + (event.outcome?.cryopods ?: 0)

        return gameSession.copy(
            integrity = integrity,
            materials = materials,
            fuel = fuel,
            cryopods = cryopods,
            launchedEvents = gameSession.launchedEvents + event.id
        )
    }

    override fun reducer(state: EventState, action: EventAction) {
        when (action) {
            EventAction.Back -> navigate(screen = Navigation.Screen.MAIN_MENU)
            is EventAction.Select -> launchInPipeline {
                if (state.gameSession == null) {
                    Logger.error(tag = TAG, message = "Invalid state: missing game session")
                    navigate(screen = Navigation.Screen.ERROR)
                    return@launchInPipeline
                }

                if (action.event == null) {
                    navigate(screen = Navigation.Screen.GAME)
                    return@launchInPipeline
                }

                val children = state.events.filter { it.parentId == action.event.id }
                val updatedGameSession = updateGameSession(
                    gameSession = state.gameSession,
                    event = action.event
                )
                gameSessionUseCases.updateGameSession(gameSession = updatedGameSession)
                updateState {
                    it.copy(
                        gameSession = updatedGameSession,
                        event = action.event,
                        children = children
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "EventStore"
    }
}
