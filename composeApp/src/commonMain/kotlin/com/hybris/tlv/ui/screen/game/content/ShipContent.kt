package com.hybris.tlv.ui.screen.game.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BedroomParent
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.space.mapper.roundTo
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun ShipContent(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()
    val gameSession = storeState.gameSession ?: return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        item {
            StatDisplay(
                icon = Icons.Outlined.Timer,
                label = getTranslation(key = "ship_years_traveled"),
                value = gameSession.yearsTraveled.roundTo(decimalPlaces = 2).toString()
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.Radar,
                label = getTranslation(key = "ship_sensor"),
                value = gameSession.sensorRange.toString()
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.Speed,
                label = getTranslation(key = "ship_speed"),
                value = "0.1c" // TODO
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.Shield,
                label = getTranslation(key = "ship_integrity"),
                value = "${gameSession.integrity} / 100",
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.LocalGasStation,
                label = getTranslation(key = "ship_fuel"),
                value = gameSession.fuel.toString()
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.Construction,
                label = getTranslation(key = "ship_materials"),
                value = gameSession.materials.toString()
            )
        }
        item {
            StatDisplay(
                icon = Icons.Outlined.BedroomParent,
                label = getTranslation(key = "ship_cryopods"),
                value = gameSession.cryopods.toString()
            )
        }
    }
}

@Composable
private fun StatDisplay(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(size = 40.dp),
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}
