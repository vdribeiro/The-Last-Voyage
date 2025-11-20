package com.hybris.tlv.ui.navigation.graph

import kotlin.reflect.typeOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.navigation.serializableType
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.event.EventStore
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.ship.model.Ship

internal fun NavGraphBuilder.eventScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<Screen.Event, EventStore>(
    navController = navController,
    typeMap = mapOf(pair = typeOf<Ship?>() to serializableType<Ship?>()),
    store = {
        EventStore(
            ship = it.ship,
            eventUseCases = useCases.event,
            gameSessionUseCases = useCases.gameSession,
        )
    },
    screen = { EventScreen(store = it) }
)
