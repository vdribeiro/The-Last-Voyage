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
import com.hybris.tlv.domain.usecase.ship.model.Engine
import com.hybris.tlv.domain.usecase.ship.model.ShipPrototype
import com.hybris.tlv.domain.usecase.translation.model.Translation
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
    val shipState = storeState.shipState
    val startTranslation = getTranslation(key = "new_game_screen__start")

    Screen(
        loading = storeState.loading,
        onBackClick = { store.send(action = NewGameAction.Back) },
        bottomBar = {
            if (storeState.loading) return@Screen
            ButtonsBar(
                buttons = persistentListOf(startTranslation),
                id = { it },
                text = { it },
                enabled = { shipState != null && shipState.remainingPoints >= 0 },
                onClick = {
                    if (shipState == null) return@ButtonsBar
                    val shipPrototype = ShipPrototype(
                        assignedPoints = shipState.assignedPoints,
                        sensorRange = shipState.sensorRange.value,
                        fuel = shipState.fuel.value,
                        materials = shipState.materials.value,
                        cryopods = shipState.cryopods.value,
                    )
                    store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
                }
            )
        },
    ) {
        if (shipState == null) return@Screen
        val sensorTranslation = getTranslation(key = "ship_sensor")
        val fuelTranslation = getTranslation(key = "ship_fuel")
        val materialsTranslation = getTranslation(key = "ship_materials")
        val cryopodsTranslation = getTranslation(key = "ship_cryopods")
        ShipConfiguration(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            remainingPoints = shipState.remainingPoints,
            attributes = persistentListOf(
                sensorTranslation to shipState.sensorRange,
                fuelTranslation to shipState.fuel,
                materialsTranslation to shipState.materials,
                cryopodsTranslation to shipState.cryopods
            ),
            attributeName = { it.first },
            attributeValue = { it.second.value },
            attributeCanIncrement = { shipState.remainingPoints > 0 && it.second.value < it.second.max },
            attributeCanDecrement = { it.second.value > it.second.min },
            onAttributeIncrement = { it.second.increment() },
            onAttributeDecrement = { it.second.decrement() },
            selectedEngineId = shipState.engine.id,
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
            Translation(
                key = "new_game_screen__continue",
                value = "Start"
            ),
        )
    )
    NewGameScreen(
        store = Store(
            initialState = NewGameState(
                loading = true,
                shipState = null,
            )
        )
    )
}

@Preview
@Composable
private fun NewGameScreenShipPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "new_game_screen__ship_points",
                value = "Points"
            ),
            Translation(
                key = "ship_sensor",
                value = "Sensor Range"
            ),
            Translation(
                key = "ship_fuel",
                value = "Fuel"
            ),
            Translation(
                key = "ship_materials",
                value = "Materials"
            ),
            Translation(
                key = "ship_cryopods",
                value = "Cryopods"
            ),
            Translation(
                key = "new_game_screen__engine_select",
                value = "Engine"
            ),
            Translation(
                key = "new_game_screen__engine_speed",
                value = "Speed"
            ),
            Translation(
                key = "new_game_screen__engine_fuel",
                value = "Fuel"
            ),
        )
    )
    NewGameScreen(
        store = Store(
            initialState = NewGameState(
                loading = false,
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
