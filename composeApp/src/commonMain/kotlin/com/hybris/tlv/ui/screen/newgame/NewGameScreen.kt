package com.hybris.tlv.ui.screen.newgame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.AttributeRow
import com.hybris.tlv.ui.theme.component.TypewriterScreen
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun NewGameScreen(store: Store<NewGameState, NewGameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val catastrophe = storeState.selectedCatastrophe
    val shipPointsTranslation = remember { getTranslation(key = "new_game_screen__ship_points") }
    val sensorTranslation = remember { getTranslation(key = "ship_sensor") }
    val fuelTranslation = remember { getTranslation(key = "ship_fuel") }
    val materialsTranslation = remember { getTranslation(key = "ship_materials") }
    val cryopodsTranslation = remember { getTranslation(key = "ship_cryopods") }
    val continueTranslation = remember { getTranslation(key = "new_game_screen__continue") }
    val startTranslation = remember { getTranslation(key = "new_game_screen__start") }

    val typography = LocalTypography.current

    TypewriterScreen(
        modifier = Modifier.testTag(tag = NEW_GAME_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        title = when (storeState.currentContent) {
            Content.SHIP -> ""
            Content.START -> catastrophe?.let { getTranslation(key = it.id) }
        },
        text = when (storeState.currentContent) {
            Content.SHIP -> ""
            Content.START -> catastrophe?.let { getTranslation(key = it.description) }
        },
        content = {
            when (storeState.currentContent) {
                Content.SHIP -> storeState.shipState?.let { shipState ->
                    Column(
                        modifier = Modifier
                            .testTag(tag = NEW_GAME_SCREEN_NEW_GAME_CONTENT)
                            .fillMaxSize()
                            .padding(all = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Remaining points
                        Text(
                            modifier = Modifier.testTag(tag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS_TEXT),
                            text = "$shipPointsTranslation: $remainingPoints",
                            style = typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(height = 16.dp))

                        // Attributes for sensor range, fuel, materials and cryopods
                        LazyColumn(
                            modifier = Modifier
                                .testTag(tag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS)
                                .weight(weight = 1f),
                            verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = Alignment.CenterVertically)
                        ) {
                            val canIncrement = remainingPoints > 0
                            item {
                                AttributeRow(
                                    name = sensorTranslation,
                                    minPoints = shipState.sensorRange.min,
                                    maxPoints = shipState.sensorRange.max,
                                    points = shipState.sensorRange.value,
                                    canIncrement = canIncrement,
                                    onIncrement = { shipState.sensorRange.increment() },
                                    onDecrement = { shipState.sensorRange.decrement() }
                                )
                            }
                            item {
                                AttributeRow(
                                    name = fuelTranslation,
                                    minPoints = shipState.fuel.min,
                                    maxPoints = shipState.fuel.max,
                                    points = shipState.fuel.value,
                                    canIncrement = canIncrement,
                                    onIncrement = { shipState.fuel.increment() },
                                    onDecrement = { shipState.fuel.decrement() }
                                )
                            }
                            item {
                                AttributeRow(
                                    name = materialsTranslation,
                                    minPoints = shipState.materials.min,
                                    maxPoints = shipState.materials.max,
                                    points = shipState.materials.value,
                                    canIncrement = canIncrement,
                                    onIncrement = { shipState.materials.increment() },
                                    onDecrement = { shipState.materials.decrement() }
                                )
                            }
                            item {
                                AttributeRow(
                                    name = cryopodsTranslation,
                                    minPoints = shipState.cryopods.min,
                                    maxPoints = shipState.cryopods.max,
                                    points = shipState.cryopods.value,
                                    canIncrement = canIncrement,
                                    onIncrement = { shipState.cryopods.increment() },
                                    onDecrement = { shipState.cryopods.decrement() }
                                )
                            }
                        }
                    }
                }

                Content.START -> null
            }
        },
        buttons = when (storeState.currentContent) {
            Content.SHIP -> listOf(continueTranslation to {
                storeState.shipState?.let { shipState ->
                    NewGameAction.SelectShip(
                        ShipPrototype(
                            assignedPoints = shipState.assignedPoints,
                            sensorRange = shipState.sensorRange.value,
                            fuel = shipState.fuel.value,
                            materials = shipState.materials.value,
                            cryopods = shipState.cryopods.value,
                        )
                    )
                }
                store.send(action = NewGameAction.Continue)
            })

            Content.START -> listOf(startTranslation to { store.send(action = NewGameAction.Continue) })
        },
    )
}

@Preview
@Composable
private fun NewGameLoading() {
    AppTheme {
        NewGameScreen(
            store = getStore(
                initialState = NewGameState(
                    loading = true,
                    currentContent = Content.SHIP,
                    selectedCatastrophe = null,
                    shipState = null,
                )
            )
        )
    }
}

@Preview
@Composable
private fun NewGameShip() {
    AppTheme {
        NewGameScreen(
            store = getStore(
                initialState = NewGameState(
                    loading = false,
                    currentContent = Content.SHIP,
                    selectedCatastrophe = null,
                    shipState = ShipState(
                        totalPoints = 10,
                        sensorRange = AttributePoint(max = 10, min = 1, interval = 1, initialValue = 3),
                        materials = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                        fuel = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                        cryopods = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                    ),
                )
            )
        )
    }
}

@Preview
@Composable
private fun NewGameStart() {
    AppTheme {
        NewGameScreen(
            store = getStore(
                initialState = NewGameState(
                    loading = false,
                    currentContent = Content.START,
                    selectedCatastrophe = Catastrophe(
                        id = "Asteroid Impact",
                        description = "A massive asteroid collides with Earth. The impact wipes out most life on the planet.",
                    ),
                    shipState = null,
                )
            )
        )
    }
}
