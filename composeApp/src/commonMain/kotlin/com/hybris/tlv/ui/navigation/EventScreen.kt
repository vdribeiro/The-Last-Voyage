package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.dependency
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.event.EventStateBuilder
import com.hybris.tlv.ui.screen.event.EventStore

internal fun NavGraphBuilder.eventGraph(navController: NavHostController) {
    composable<EventScreen> { entry ->
        val args = entry.toRoute<EventScreen>()
        val store = viewModel {
            EventStore(
                stateBuilder = args.stateBuilder,
                eventUseCases = dependency.useCases.event,
                gameSessionUseCases = dependency.useCases.gameSession,
            )
        }
        LifecycleCoroutine(store) {
            store.effect.collect { screen ->
                when (screen) {
                    is GameScreen -> navController.popBackStack()
                }
            }
        }
        EventScreen(store = store)
    }
}

@Serializable
internal data class EventScreen(val stateBuilder: EventStateBuilder = EventStateBuilder.Default): Screen
