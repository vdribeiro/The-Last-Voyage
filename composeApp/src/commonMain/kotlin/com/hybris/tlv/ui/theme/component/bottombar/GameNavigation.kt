package com.hybris.tlv.ui.theme.component.bottombar

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun GameNavigation(
    modifier: Modifier = Modifier,
    shipEnabled: Boolean = true,
    shipSelected: Boolean = false,
    shipOnClick: () -> Unit = {},
    systemEnabled: Boolean = true,
    systemSelected: Boolean = false,
    systemOnClick: () -> Unit = {},
    travelEnabled: Boolean = true,
    travelSelected: Boolean = false,
    travelOnClick: () -> Unit = {},
) {
    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val travelTranslation = remember(key1 = translationVersion) { getTranslation(key = "game_screen__travel") }
    val systemTranslation = remember(key1 = translationVersion) { getTranslation(key = "game_screen__system") }
    val shipTranslation = remember(key1 = translationVersion) { getTranslation(key = "game_screen__ship") }

    NavigationBar(
        modifier = modifier,
        items = listOf(
            NavigationItem(
                label = shipTranslation,
                icon = Icons.Filled.Rocket,
                enabled = shipEnabled,
                selected = shipSelected,
                onClick = shipOnClick
            ),
            NavigationItem(
                label = systemTranslation,
                icon = Icons.Filled.Hub,
                enabled = systemEnabled,
                selected = systemSelected,
                onClick = systemOnClick
            ),
            NavigationItem(
                label = travelTranslation,
                icon = Icons.Filled.RocketLaunch,
                enabled = travelEnabled,
                selected = travelSelected,
                onClick = travelOnClick
            )
        )
    )
}

@Preview
@Composable
private fun BottomNavigationPreview() = AppTheme {
    GameNavigation()
}
