package com.hybris.tlv.ui.screen.event

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.StatusBar
import com.hybris.tlv.ui.theme.component.TypewriterScreen
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.TravelOutcome
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun EventScreen(store: Store<EventState, EventAction>) {
    val storeState by store.stateFlow.collectAsState()
    val event = storeState.parentEvent
    val children = storeState.childrenEvents
    val ship = storeState.ship
    val title = if (event?.id != null) getTranslation(key = event.id) else ""
    val outcome = if (event?.outcome != null) "\n\n${event.outcome.getTranslation()}" else ""
    val text = if (event?.description != null) getTranslation(key = event.description) + outcome else ""

    TypewriterScreen(
        modifier = Modifier.testTag(tag = EVENT_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        topBar = {
            StatusBar(
                modifier = Modifier
                    .testTag(tag = EVENT_SCREEN_STATUS_BAR)
                    .statusBarsPadding(),
                hull = ship?.integrity?.toString(),
                fuel = ship?.fuel?.toString(),
                materials = ship?.materials?.toString(),
                cryopods = ship?.cryopods?.toString()
            )
        },
        title = title,
        text = text,
        buttons = children.map { getTranslation(key = it.id) to { store.send(action = EventAction.Select(event = it)) } }
    )
}

@Preview
@Composable
private fun EventLoading() = AppTheme {
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
private fun EventRandom() = AppTheme {
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
                        velocity = 0.1
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
