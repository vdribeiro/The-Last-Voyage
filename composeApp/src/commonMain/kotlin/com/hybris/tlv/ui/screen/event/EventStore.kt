package com.hybris.tlv.ui.screen.event

import androidx.annotation.VisibleForTesting
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
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
    audioPlayer: AudioPlayer,
    state: EventState?,
    private val eventUseCases: EventUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<EventState, EventAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = state ?: EventState(
        loading = true,
        gameSession = null,
        ship = null,
        eventChain = emptyList(),
        parentEvent = defaultEvent,
        childrenEvents = emptyList(),
    )
) {
    init {
        if (state == null) setup()
    }

    private fun setup(): Job = launch {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackStateBuilder(tag = TAG, message = "Invalid state: missing game session on setup()"))
            return@launch
        }

        // Guarantee at least 1 event
        val eventChain = eventUseCases.getRandomEvent(ids = gameSession.launchedEvents).ifEmpty {
            listOf(element = defaultEvent)
        }

        // There must be at least 1 event with no parentId, this is the parent event
        val parentEvent = eventChain.find { it.parentId == null }
        if (parentEvent == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackStateBuilder(tag = TAG, message = "Invalid state: missing parent event on setup()"))
            return@launch
        }
        val childrenEvents = eventChain.filter { it.parentId == parentEvent.id }.ifEmpty {
            listOf(element = stopEvent)
        }
        val updatedGameSession = gameSessionUseCases.launchEvent(gameSession = gameSession, event = parentEvent)

        updateState {
            it.copy(
                loading = false,
                gameSession = updatedGameSession,
                ship = updatedGameSession.ship,
                eventChain = eventChain,
                parentEvent = parentEvent,
                childrenEvents = childrenEvents,
            )
        }
    }

    private fun select(state: EventState, action: EventAction.Select): Job = launch {
        // Event chain has ended
        if (action.event == stopEvent) {
            navigate(screen = Screen.GAME)
            return@launch
        }

        val gameSession = state.gameSession
        if (gameSession == null) {
            navigate(screen = Screen.FEEDBACK, state = FeedbackStateBuilder(tag = TAG, message = "Invalid state: missing game session on select()"))
            return@launch
        }

        // Continue event chain
        val childrenEvents = state.eventChain.filter { it.parentId == action.event.id }.ifEmpty {
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
            is EventAction.Select -> select(state = state, action = action)
        }
    }

    companion object {
        private const val TAG = "EventStore"
    }
}
