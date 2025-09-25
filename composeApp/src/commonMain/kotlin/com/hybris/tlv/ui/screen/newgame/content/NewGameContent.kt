package com.hybris.tlv.ui.screen.newgame.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.newgame.NewGameAction
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.AttributeRow
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun NewGameContent(store: Store<NewGameAction, NewGameState>) {
    val storeState by store.stateFlow.collectAsState()
    val shipState = storeState.shipState
    val remainingPoints = shipState.remainingPoints
    val shipPointsTranslation = remember { getTranslation(key = "new_game_screen__ship_points") }
    val sensorTranslation = remember { getTranslation(key = "ship_sensor") }
    val fuelTranslation = remember { getTranslation(key = "ship_fuel") }
    val materialsTranslation = remember { getTranslation(key = "ship_materials") }
    val cryopodsTranslation = remember { getTranslation(key = "ship_cryopods") }
    val continueTranslation = remember { getTranslation(key = "new_game_screen__continue") }

    val typography = LocalTypography.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Remaining points
        Text(
            text = "$shipPointsTranslation: $remainingPoints",
            style = typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(height = 16.dp))

        // Sliders for sensor range, fuel, materials and cryopods
        LazyColumn(
            modifier = Modifier.weight(weight = 1f),
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
        Spacer(modifier = Modifier.weight(weight = 1f))

        // Continue button
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            onClick = {
                store.send(
                    action = NewGameAction.SelectShip(
                        ShipPrototype(
                            assignedPoints = shipState.assignedPoints,
                            sensorRange = shipState.sensorRange.value,
                            fuel = shipState.fuel.value,
                            materials = shipState.materials.value,
                            cryopods = shipState.cryopods.value,
                        )
                    )
                )
                store.send(action = NewGameAction.Start)
            },
        ) {
            Text(text = continueTranslation)
        }
    }
}
