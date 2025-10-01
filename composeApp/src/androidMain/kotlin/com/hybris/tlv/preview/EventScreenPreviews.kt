package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.screen.event.EventScreen
import com.hybris.tlv.ui.screen.event.EventState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.event.model.Event
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.TravelOutcome
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Preview
@Composable
private fun EventLoading() {
    AppTheme {
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
}

@Preview
@Composable
private fun EventRandom() {
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
    AppTheme {
        EventScreen(
            store = getStore(
                initialState = EventState(
                    loading = false,
                    ship = Ship(
                        id = "1",
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
}
