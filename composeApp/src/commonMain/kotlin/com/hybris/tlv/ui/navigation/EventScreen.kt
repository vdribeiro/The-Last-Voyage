package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.event.EventAction
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.screen.event.EventStateBuilder
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.eventScreen(
    navController: NavHostController,
    useCases: UseCases
) {
    graph<EventScreen, EventState, EventAction>(
        navController = navController,
        createStore = {
            EventStore(
                stateBuilder = it.stateBuilder,
                eventUseCases = useCases.event,
                gameSessionUseCases = useCases.gameSession,
            )
        },
        content = { EventScreen(store = it) }
    )
}

@Serializable
internal data class EventScreen(val stateBuilder: EventStateBuilder = EventStateBuilder.Default): Screen
