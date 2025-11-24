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
import androidx.compose.ui.graphics.vector.ImageVector
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun GameNavigationBar(
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
    val translationVersion by TranslationCache.versionFlow.collectAsState()
    val travelTranslation = remember(key1 = translationVersion) { getTranslation(key = "game_screen__travel") }
    val systemTranslation = remember(key1 = translationVersion) { getTranslation(key = "game_screen__system") }
    val shipTranslation = remember(key1 = translationVersion) { getTranslation(key = "game_screen__ship") }

    NavigationBar(
        modifier = modifier,
        items = listOf(
            GameNavigationItem(
                enabled = shipEnabled,
                selected = shipSelected,
                text = shipTranslation,
                icon = Icons.Filled.Rocket,
                onClick = shipOnClick
            ),
            GameNavigationItem(
                enabled = systemEnabled,
                selected = systemSelected,
                text = systemTranslation,
                icon = Icons.Filled.Hub,
                onClick = systemOnClick
            ),
            GameNavigationItem(
                enabled = travelEnabled,
                selected = travelSelected,
                text = travelTranslation,
                icon = Icons.Filled.RocketLaunch,
                onClick = travelOnClick
            )
        ),
        enabled = { it.enabled },
        selected = { it.selected },
        text = { it.text },
        icon = { it.icon },
        onClick = { it.onClick() }
    )
}

private data class GameNavigationItem(
    val enabled: Boolean,
    val selected: Boolean,
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Preview
@Composable
private fun GameNavigationBarPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "game_screen__travel",
                value = "Travel"
            ),
            Translation(
                key = "game_screen__system",
                value = "System"
            ),
            Translation(
                key = "game_screen__ship",
                value = "Ship"
            ),
        )
    )
    GameNavigationBar()
}
