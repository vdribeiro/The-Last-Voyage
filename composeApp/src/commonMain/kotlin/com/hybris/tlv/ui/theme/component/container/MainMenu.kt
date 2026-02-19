package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.AppLogo
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun MainMenu(
    modifier: Modifier = Modifier,
    onScoresClick: () -> Unit = {},
    onAchievementsClick: () -> Unit = {},
    onStellarExplorerClick: () -> Unit = {},
    onNewGameClick: () -> Unit = {},
    ongoingGameSession: Boolean = false,
    onOngoingGameSessionClick: () -> Unit = {},
) {
    val appNameTranslation = getTranslation(key = "app_name")
    val newGameTranslation = getTranslation(key = "main_menu_screen__new_game")
    val continueTranslation = getTranslation(key = "main_menu_screen__continue")
    val stellarExplorerTranslation = getTranslation(key = "main_menu_screen__stellar_explorer")
    val scoresTranslation = getTranslation(key = "main_menu_screen__scores")
    val achievementsTranslation = getTranslation(key = "main_menu_screen__achievements")

    val typography = LocalTypography.current

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        scrollBar = false
    ) {
        val menuItem = @Composable { text: String, onClick: () -> Unit ->
            Text(
                modifier = Modifier
                    .clickable { onClick() },
                text = text,
                style = typography.headlineMedium,
            )
        }
        item {
            AppLogo(
                modifier = Modifier.padding(bottom = 16.dp),
                showBackground = false,
                text = appNameTranslation
            )
        }
        item { menuItem(newGameTranslation, onNewGameClick) }
        if (ongoingGameSession) item { menuItem(continueTranslation, onOngoingGameSessionClick) }
        item { menuItem(stellarExplorerTranslation, onStellarExplorerClick) }
        item { menuItem(scoresTranslation, onScoresClick) }
        item { menuItem(achievementsTranslation, onAchievementsClick) }
    }
}

@Preview
@Composable
private fun MainMenuPreview() = AppTheme {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "app_name",
                value = "TLV"
            ),
            Translation(
                key = "main_menu_screen__new_game",
                value = "New Game"
            ),
            Translation(
                key = "main_menu_screen__continue",
                value = "Continue"
            ),
            Translation(
                key = "main_menu_screen__stellar_explorer",
                value = "Explorer"
            ),
            Translation(
                key = "main_menu_screen__scores",
                value = "Scores"
            ),
            Translation(
                key = "main_menu_screen__achievements",
                value = "Achievements"
            ),
        )
    )
    MainMenu(ongoingGameSession = true)
}