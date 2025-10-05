package com.hybris.tlv.ui.theme.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun BottomNavigation(
    modifier: Modifier = Modifier,
    shipSelected: Boolean = false,
    shipOnClick: () -> Unit = {},
    systemSelected: Boolean = false,
    systemOnClick: () -> Unit = {},
    travelSelected: Boolean = false,
    travelOnClick: () -> Unit = {},
) {
    val travelTranslation = remember { getTranslation(key = "game_screen__travel") }
    val systemTranslation = remember { getTranslation(key = "game_screen__system") }
    val shipTranslation = remember { getTranslation(key = "game_screen__ship") }

    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Rocket, contentDescription = shipTranslation) },
            label = { Text(text = shipTranslation) },
            selected = shipSelected,
            onClick = shipOnClick,
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.Hub, contentDescription = systemTranslation) },
            label = { Text(text = systemTranslation) },
            selected = systemSelected,
            onClick = systemOnClick,
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = travelTranslation) },
            label = { Text(text = travelTranslation) },
            selected = travelSelected,
            onClick = travelOnClick,
        )
    }
}
