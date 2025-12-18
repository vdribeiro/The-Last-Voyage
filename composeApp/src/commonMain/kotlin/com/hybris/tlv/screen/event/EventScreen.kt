package com.hybris.tlv.screen.event

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.screen.Screen
import com.hybris.tlv.screen.Store
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.theme.component.bottombar.BottomButton
import com.hybris.tlv.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.theme.component.container.TypewriterContent
import com.hybris.tlv.theme.component.topbar.StatusBar
import com.hybris.tlv.theme.getTranslation
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.TravelOutcome
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun EventScreen(store: Store<EventState, EventAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val ship = storeState.ship
    val event = storeState.parentEvent

    Screen(
        store = store,
        loading = storeState.loading,
        back = false,
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp
                ),
                hull = ship?.integrity?.toString(),
                fuel = ship?.fuel?.toString(),
                materials = ship?.materials?.toString(),
                cryopods = ship?.cryopods?.toString()
            )
        },
        bottomBar = {
            ButtonsBar(
                buttons = storeState.childrenEvents.map {
                    BottomButton(
                        id = it.id,
                        text = getTranslation(key = it.id),
                        onClick = { store.send(action = EventAction.Select(event = it)) }
                    )
                }
            )
        },
    ) {
        event?.let { event ->
            val outcome = event.outcome?.let {
                with(receiver = it) {
                    buildList {
                        add("\n")
                        if (integrity != null) add("${if (integrity > 0) "+" else ""}$integrity ${getTranslation(key = "ship_integrity")}")
                        if (materials != null) add("${if (materials > 0) "+" else ""}$materials ${getTranslation(key = "ship_materials")}")
                        if (fuel != null) add("${if (fuel > 0.0) "+" else ""}$fuel ${getTranslation(key = "ship_fuel")}")
                        if (cryopods != null) add("${if (cryopods > 0) "+" else ""}$cryopods ${getTranslation(key = "ship_cryopods")}")
                    }.joinToString(separator = "\n")
                }
            }.orEmpty()
            TypewriterContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = event.id),
                text = "${getTranslation(key = event.description)}$outcome",
            )
        }
    }
}

@Preview
@Composable
private fun EventScreenLoadingPreview() = AppTheme {
    EventScreen(
        store = Store(
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
private fun EventScreenPreview() = AppTheme {
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
        store = Store(
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
