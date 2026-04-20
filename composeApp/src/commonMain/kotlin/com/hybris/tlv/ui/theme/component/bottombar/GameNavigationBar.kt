package com.hybris.tlv.ui.theme.component.bottombar

import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.theme.PreviewTranslation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.getTranslation

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
    val shipTranslation = getTranslation(key = "game_screen__ship")
    val systemTranslation = getTranslation(key = "game_screen__system")
    val travelTranslation = getTranslation(key = "game_screen__travel")

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        items = persistentListOf(
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
private fun GameNavigationBarPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            PreviewTranslation(
                key = "game_screen__travel",
                value = "Travel"
            ),
            PreviewTranslation(
                key = "game_screen__system",
                value = "System"
            ),
            PreviewTranslation(
                key = "game_screen__ship",
                value = "Ship"
            ),
        )
    )
    GameNavigationBar()
}
