package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.image.AppLogo
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

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
        item {
            AppLogo(
                modifier = Modifier.padding(bottom = 16.dp),
                showBackground = false,
                text = appNameTranslation
            )
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { onNewGameClick() },
                text = newGameTranslation,
                style = typography.headlineMedium,
            )
        }
        if (ongoingGameSession) {
            item {
                Text(
                    modifier = Modifier
                        .clickable { onOngoingGameSessionClick() },
                    text = continueTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { onStellarExplorerClick() },
                text = stellarExplorerTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { onScoresClick() },
                text = scoresTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { onAchievementsClick() },
                text = achievementsTranslation,
                style = typography.headlineMedium,
            )
        }
    }
}

@Preview
@Composable
private fun MainMenuPreview() = AppTheme {
    TranslationCache.set(
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