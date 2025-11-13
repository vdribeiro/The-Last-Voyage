package com.hybris.tlv.ui.screen.event

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.getStore
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.container.TypewriterContent
import com.hybris.tlv.ui.theme.component.topbar.StatusBar
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.TravelOutcome
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun EventScreen(store: Store<EventState, EventAction>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = storeState.ship
    val event = storeState.parentEvent

    Screen(
        loading = storeState.loading,
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier
                    .statusBarsPadding(),
                hull = ship?.integrity?.toString(),
                fuel = ship?.fuel?.toString(),
                materials = ship?.materials?.toString(),
                cryopods = ship?.cryopods?.toString()
            )
        },
        bottomBar = {
            ButtonsBar(
                buttons = storeState.childrenEvents.map {
                    getTranslation(key = it.id) to { store.send(action = EventAction.Select(event = it)) }
                }
            )
        },
    ) {
        TypewriterContent(
            title = event?.let { getTranslation(key = it.id) },
            text = event?.let { "${getTranslation(key = it.description)}${it.outcome?.getTranslation()}" },
        )
    }
}

@Preview
@Composable
private fun EventLoadingPreview() = AppTheme {
    EventScreen(
        store = getStore(
            initialState = EventState(
                loading = true,
                ship = null,
                parentEvent = null,
                childrenEvents = emptyList()
            )
        )
    )
}

@Preview
@Composable
private fun EventRandomPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "ship_materials",
                value = "Materials"
            ),
            Translation(
                key = "ship_fuel",
                value = "Fuel"
            ),
        )
    )
    EventScreen(
        store = getStore(
            initialState = EventState(
                loading = false,
                ship = Ship(
                    id = "1",
                    engine = Engine(
                        id = "1",
                        description = "",
                        velocity = 0.1,
                        fuelConsumption = 0.0,
                        cost = 0
                    ),
                    assignedPoints = 10,
                    yearsTraveled = 100.0,
                    sensorRange = 5,
                    integrity = 80,
                    fuel = 100,
                    materials = 90,
                    cryopods = 150,
                ),
                parentEvent = Event(
                    id = "Two Buttons",
                    description = "You have a green and a red button.",
                    parentId = null,
                    outcome = TravelOutcome(
                        materials = -4,
                        fuel = -2,
                    ),
                ),
                childrenEvents = listOf(
                    Event(
                        id = "Press the Red Button",
                        description = "Lose",
                        parentId = null,
                        outcome = null
                    ),
                    Event(
                        id = "Press the Green Button",
                        description = "Win",
                        parentId = null,
                        outcome = null
                    )
                )
            )
        )
    )
}
