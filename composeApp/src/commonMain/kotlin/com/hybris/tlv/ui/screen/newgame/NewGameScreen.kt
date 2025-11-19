package com.hybris.tlv.ui.screen.newgame

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.ui.theme.component.button.AttributePoint
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.container.TypewriterContent
import com.hybris.tlv.ui.theme.component.list.ShipConfiguration
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun NewGameScreen(store: Store<NewGameState, NewGameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val continueTranslation = remember(key1 = translationVersion) { getTranslation(key = "new_game_screen__continue") }
    val startTranslation = remember(key1 = translationVersion) { getTranslation(key = "new_game_screen__start") }

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            ButtonsBar(
                buttons = when (storeState.currentContent) {
                    Content.SHIP -> buildList {
                        val shipState = storeState.shipState
                        val onClick = when {
                            shipState?.remainingPoints != null && shipState.remainingPoints >= 0 -> {
                                {
                                    val shipPrototype = ShipPrototype(
                                        assignedPoints = shipState.assignedPoints,
                                        sensorRange = shipState.sensorRange.value,
                                        fuel = shipState.fuel.value,
                                        materials = shipState.materials.value,
                                        cryopods = shipState.cryopods.value,
                                    )
                                    store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
                                }
                            }

                            else -> null
                        }
                        add(element = continueTranslation to onClick)
                    }

                    Content.START -> listOf(startTranslation to { store.send(action = NewGameAction.Next) })
                }
            )
        },
    ) {
        when (storeState.currentContent) {
            Content.SHIP -> {
                val shipState = storeState.shipState ?: return@Screen
                ShipConfiguration(
                    remainingPoints = shipState.remainingPoints,
                    sensorRange = shipState.sensorRange,
                    fuel = shipState.fuel,
                    materials = shipState.materials,
                    cryopods = shipState.cryopods,
                    selectedEngineId = shipState.engine.id,
                    engines = storeState.engines,
                    id = { it.id },
                    description = { it.description },
                    velocity = { it.velocity },
                    fuelConsumption = { it.fuelConsumption },
                    cost = { it.cost },
                    onEngineClick = { engine -> store.send(action = NewGameAction.SelectEngine(engine = engine)) }
                )
            }

            Content.START -> TypewriterContent(
                title = storeState.selectedCatastrophe?.let { getTranslation(key = it.id) },
                text = storeState.selectedCatastrophe?.let { getTranslation(key = it.description) }
            )
        }
    }
}

@Preview
@Composable
private fun NewGameLoadingPreview() = AppTheme {
    NewGameScreen(
        store = Store(
            initialState = NewGameState(
                loading = true,
                currentContent = Content.SHIP,
                selectedCatastrophe = null,
                shipState = null,
            )
        )
    )
}

@Preview
@Composable
private fun NewGameShipPreview() = AppTheme {
    NewGameScreen(
        store = Store(
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
                    engine = Engine(
                        id = "Engine",
                        description = "Engine description",
                        velocity = 10.0,
                        fuelConsumption = 1.0,
                        cost = 1
                    )
                ),
            )
        )
    )
}

@Preview
@Composable
private fun NewGameStartPreview() = AppTheme {
    NewGameScreen(
        store = Store(
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
