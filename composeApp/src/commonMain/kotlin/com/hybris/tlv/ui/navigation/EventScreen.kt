package com.hybris.tlv.ui.navigation

import kotlin.reflect.typeOf
import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.ship.model.Ship

internal fun NavGraphBuilder.eventScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<EventScreen, EventStore>(
    navController = navController,
    typeMap = mapOf(typeOf<Ship?>() to serializableType<Ship?>()),
    store = {
        EventStore(
            ship = it.ship,
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession,
        )
    },
    screen = { EventScreen(store = it) }
)

@Serializable
internal data class EventScreen(val ship: Ship? = null) : Screen
