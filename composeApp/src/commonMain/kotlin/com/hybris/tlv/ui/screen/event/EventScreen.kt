package com.hybris.tlv.ui.screen.event

import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.event.Event
import com.hybris.tlv.domain.event.TravelOutcome
import com.hybris.tlv.domain.ship.Engine
import com.hybris.tlv.domain.ship.Ship
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.ui.theme.component.container.TypewriterContent
import com.hybris.tlv.ui.theme.component.topbar.StatusBar
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun EventScreen(store: Store<EventState, EventAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val ship = storeState.ship
    val event = storeState.parentEvent

    val integrityTranslation = getTranslation(key = "ship_integrity")
    val fuelTranslation = getTranslation(key = "ship_fuel")
    val materialsTranslation = getTranslation(key = "ship_materials")
    val cryopodsTranslation = getTranslation(key = "ship_cryopods")

    Screen(
        loading = storeState.loading,
        onBackClick = null,
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
                modifier = Modifier.testTag(tag = "event_buttons_bar"),
                buttons = storeState.childrenEvents,
                id = Event::id,
                text = { getTranslation(key = it.id) },
                onClick = { store.send(action = EventAction.Select(event = it)) }
            )
        },
    ) {
        event?.let { event ->
            val outcome = remember(key1 = event.outcome) {
                event.outcome?.let { outcome ->
                    buildList {
                        add(element = "\n")
                        outcome.integrity?.let { add(element = "${if (it > 0) "+" else ""}$it $integrityTranslation") }
                        outcome.materials?.let { add(element = "${if (it > 0) "+" else ""}$it $materialsTranslation") }
                        outcome.fuel?.let { add(element = "${if (it > 0.0) "+" else ""}$it $fuelTranslation") }
                        outcome.cryopods?.let { add(element = "${if (it > 0) "+" else ""}$it $cryopodsTranslation") }
                    }.joinToString(separator = "\n")
                }.orEmpty()
            }
            TypewriterContent(
                modifier = Modifier
                    .testTag(tag = "event_content")
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
private fun EventScreenLoadingPreview() = Preview {
    EventScreen(
        store = Store(
            initialState = EventState(
                loading = true,
                ship = null,
                parentEvent = null,
                childrenEvents = persistentListOf()
            )
        )
    )
}

@Preview
@Composable
private fun EventScreenPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "ship_materials",
                value = "Materials"
            ),
            PreviewTranslation(
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
                childrenEvents = persistentListOf(
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
