package com.hybris.tlv.ui.screen.newgame

import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.ship.Engine
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.ui.theme.component.container.ShipConfiguration
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun NewGameScreen(store: Store<NewGameState, NewGameAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val startTranslation = getTranslation(key = "new_game_screen__start")

    Screen(
        loading = storeState.loading,
        onBackClick = { store.send(action = NewGameAction.Back) },
        bottomBar = {
            if (storeState.loading) return@Screen
            ButtonsBar(
                buttons = persistentListOf(startTranslation),
                id = { it },
                text = { getTranslation(key = it) },
                enabled = { storeState.remainingPoints >= 0 },
                onClick = {
                    store.send(action = NewGameAction.SelectShip)
                }
            )
        },
    ) {
        val sensorTranslation = getTranslation(key = "ship_sensor")
        val fuelTranslation = getTranslation(key = "ship_fuel")
        val materialsTranslation = getTranslation(key = "ship_materials")
        val cryopodsTranslation = getTranslation(key = "ship_cryopods")
        ShipConfiguration(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            remainingPoints = storeState.remainingPoints,
            attributes = persistentListOf(
                sensorTranslation to storeState.sensorRange,
                fuelTranslation to storeState.fuel,
                materialsTranslation to storeState.materials,
                cryopodsTranslation to storeState.cryopods
            ),
            attributeName = { it.first },
            attributeValue = { it.second.value },
            attributeCanIncrement = { storeState.remainingPoints > 0 && it.second.value < it.second.max },
            attributeCanDecrement = { it.second.value > it.second.min },
            onAttributeIncrement = { store.send(action = NewGameAction.Increment(attributePoint = it.second)) },
            onAttributeDecrement = { store.send(action = NewGameAction.Decrement(attributePoint = it.second)) },
            selectedEngineId = storeState.selectedEngine?.id,
            engines = storeState.engines,
            engineId = Engine::id,
            engineName = { getTranslation(key = it.id) },
            engineDescription = { getTranslation(key = it.description) },
            engineVelocity = Engine::velocity,
            engineFuelConsumption = Engine::fuelConsumption,
            engineCost = Engine::cost,
            onEngineClick = { engine -> store.send(action = NewGameAction.SelectEngine(engine = engine)) }
        )
    }
}

@Preview
@Composable
private fun NewGameScreenLoadingPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "new_game_screen__continue",
                value = "Start"
            ),
        )
    )
    NewGameScreen(
        store = Store(
            initialState = NewGameState(
                loading = true
            )
        )
    )
}

@Preview
@Composable
private fun NewGameScreenShipPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "new_game_screen__ship_points",
                value = "Points"
            ),
            PreviewTranslation(
                key = "ship_sensor",
                value = "Sensor Range"
            ),
            PreviewTranslation(
                key = "ship_fuel",
                value = "Fuel"
            ),
            PreviewTranslation(
                key = "ship_materials",
                value = "Materials"
            ),
            PreviewTranslation(
                key = "ship_cryopods",
                value = "Cryopods"
            ),
            PreviewTranslation(
                key = "new_game_screen__engine_select",
                value = "Engine"
            ),
            PreviewTranslation(
                key = "new_game_screen__engine_speed",
                value = "Speed"
            ),
            PreviewTranslation(
                key = "new_game_screen__engine_fuel",
                value = "Fuel"
            ),
        )
    )
    NewGameScreen(
        store = Store(
            initialState = NewGameState(
                loading = false,
                sensorRange = AttributePoint(type = Attribute.SENSOR_RANGE, max = 10, min = 1, interval = 1, value = 3),
                materials = AttributePoint(type = Attribute.MATERIALS, max = 1000, min = 0, interval = 100, value = 100),
                fuel = AttributePoint(type = Attribute.FUEL, max = 1000, min = 0, interval = 100, value = 100),
                cryopods = AttributePoint(type = Attribute.CRYOPODS, max = 1000, min = 0, interval = 100, value = 100),
                engines = persistentListOf(
                    Engine(
                        id = "Nuclear",
                        description = "BadaBoom",
                        velocity = 10.0,
                        fuelConsumption = 1.0,
                        cost = 10
                    ),
                    Engine(
                        id = "Chemical",
                        description = "BadaBing",
                        velocity = 1.0,
                        fuelConsumption = 10.0,
                        cost = 1
                    )
                ),
                selectedEngine = Engine(
                    id = "Nuclear",
                    description = "BadaBoom",
                    velocity = 10.0,
                    fuelConsumption = 1.0,
                    cost = 1
                )
            )
        )
    )
}
