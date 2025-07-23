package com.hybris.tlv.ui.screen.newgame.content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hybris.tlv.ui.screen.newgame.NewGameAction
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun NewGameContent(store: Store<NewGameAction, NewGameState>) {
    val storeState by store.stateFlow.collectAsState()

    // number of attributes * max points per attribute
    val totalPoints = 40

    var sensorRange by remember { mutableIntStateOf(value = storeState.selectedShip.sensorRange) }
    var materials by remember { mutableIntStateOf(value = storeState.selectedShip.materials) }
    var fuel by remember { mutableIntStateOf(value = storeState.selectedShip.fuel) }
    var cryopods by remember { mutableIntStateOf(value = storeState.selectedShip.cryopods) }

    val assignedPoints = sensorRange + (materials / 100) + (fuel / 100) + (cryopods / 100)
    val remainingPoints = totalPoints - assignedPoints

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "${getTranslation(key = "new_game_screen__ship_points")}: $remainingPoints",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(height = 16.dp))

        LazyColumn(
            modifier = Modifier.weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = Alignment.CenterVertically)
        ) {
            val canIncrement = remainingPoints > 0
            item {
                AttributeRow(
                    name = getTranslation(key = "ship_sensor"),
                    minPoints = 1,
                    maxPoints = 10,
                    points = sensorRange,
                    canIncrement = canIncrement,
                    onIncrement = { sensorRange++ },
                    onDecrement = { sensorRange-- }
                )
            }
            item {
                AttributeRow(
                    name = getTranslation(key = "ship_fuel"),
                    minPoints = 100,
                    maxPoints = 1000,
                    points = fuel,
                    canIncrement = canIncrement,
                    onIncrement = { fuel += 100 },
                    onDecrement = { fuel -= 100 }
                )
            }
            item {
                AttributeRow(
                    name = getTranslation(key = "ship_materials"),
                    minPoints = 100,
                    maxPoints = 1000,
                    points = materials,
                    canIncrement = canIncrement,
                    onIncrement = { materials += 100 },
                    onDecrement = { materials -= 100 }
                )
            }
            item {
                AttributeRow(
                    name = getTranslation(key = "ship_cryopods"),
                    minPoints = 100,
                    maxPoints = 1000,
                    points = cryopods,
                    canIncrement = canIncrement,
                    onIncrement = { cryopods += 100 },
                    onDecrement = { cryopods -= 100 }
                )
            }
        }

        Spacer(modifier = Modifier.weight(weight = 1f))

        // For cheaters only
        //Button(
        //    modifier = Modifier
        //        .fillMaxWidth(),
        //    colors = ButtonDefaults.buttonColors(contentColor = Color.White),
        //    onClick = {
        //        store.send(
        //            action = NewGameAction.SelectShip(
        //                ShipPrototype(
        //                    assignedPoints = assignedPoints,
        //                    sensorRange = sensorRange,
        //                    materials = materials,
        //                    fuel = fuel,
        //                    cryopods = cryopods
        //                )
        //            )
        //        )
        //        store.send(action = NewGameAction.Advanced)
        //    },
        //) {
        //    Text(text = getTranslation(key = "new_game_screen__advanced"))
        //}

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            onClick = {
                store.send(
                    action = NewGameAction.SelectShip(
                        ShipPrototype(
                            assignedPoints = assignedPoints,
                            sensorRange = sensorRange,
                            materials = materials,
                            fuel = fuel,
                            cryopods = cryopods
                        )
                    )
                )
                store.send(action = NewGameAction.Start)
            },
        ) {
            Text(text = getTranslation(key = "new_game_screen__continue"))
        }
    }
}

@Composable
private fun AttributeRow(
    modifier: Modifier = Modifier,
    name: String,
    minPoints: Int,
    maxPoints: Int,
    points: Int,
    canIncrement: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onDecrement, enabled = points > minPoints) {
            Icon(
                modifier = Modifier.size(size = 36.dp),
                imageVector = Icons.Default.RemoveCircle,
                contentDescription = "-$name",
            )
        }

        Column(
            modifier = Modifier.weight(weight = 1f).padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "$name: $points",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(height = 4.dp))
            val progress = if (minPoints >= maxPoints) 0.0f else (points.toFloat() - minPoints) / (maxPoints.toFloat() - minPoints)
            val animatedProgress by animateFloatAsState(targetValue = progress.coerceIn(minimumValue = 0.0f, maximumValue = 1.0f))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().height(height = 8.dp),
                progress = { animatedProgress },
            )
        }

        IconButton(onClick = onIncrement, enabled = canIncrement && points < maxPoints) {
            Icon(
                modifier = Modifier.size(size = 36.dp),
                imageVector = Icons.Default.AddCircle,
                contentDescription = "+$name",
            )
        }
    }
}
