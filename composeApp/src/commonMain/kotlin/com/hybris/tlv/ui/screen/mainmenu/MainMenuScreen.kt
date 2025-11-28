package com.hybris.tlv.ui.screen.mainmenu

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.bottombar.MainBar
import com.hybris.tlv.ui.theme.component.container.MainMenu
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun MainMenuScreen(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsState()

    Screen(
        loading = storeState.loading,
        banner = if (storeState.newVersionBanner) storeState.developerCorner else null,
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            MainBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                onCreditsClick = { store.send(action = MainMenuAction.Credits) },
                developerCornerUri = storeState.developerCorner,
                supportUri = storeState.support
            )
        },
    ) {
        MainMenu(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            onScoresClick = { store.send(action = MainMenuAction.Scores) },
            onAchievementsClick = { store.send(action = MainMenuAction.Achievements) },
            onStellarExplorerClick = { store.send(action = MainMenuAction.StellarExplorer) },
            onNewGameClick = { store.send(action = MainMenuAction.NewGame) },
            ongoingGameSession = storeState.ongoingGameSession,
            onOngoingGameSessionClick = { store.send(action = MainMenuAction.Next) }
        )
    }
}

@Preview
@Composable
private fun MainMenuScreenLoadingPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "website",
                value = "Website"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    MainMenuScreen(
        store = Store(
            initialState = MainMenuState(
                loading = true,
                developerCorner = "Developer Corner",
                support = "Support",
                ongoingGameSession = false,
            )
        )
    )
}

@Preview
@Composable
private fun MainMenuScreenAllPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = "The Last Voyage"
            ),
            Translation(
                key = "main_menu_screen__new_game",
                value = "New Game"
            ),
            Translation(
                key = "main_menu_screen__stellar_explorer",
                value = "Stellar Explorer"
            ),
            Translation(
                key = "main_menu_screen__scores",
                value = "Scores"
            ),
            Translation(
                key = "main_menu_screen__achievements",
                value = "Achievements"
            ),
            Translation(
                key = "website",
                value = "Website"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    MainMenuScreen(
        store = Store(
            initialState = MainMenuState(
                loading = false,
                developerCorner = "Developer Corner",
                support = "Support",
                ongoingGameSession = false,
            )
        )
    )
}

@Preview
@Composable
private fun MainMenuScreenContinuePreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = "The Last Voyage"
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
                value = "Stellar Explorer"
            ),
            Translation(
                key = "main_menu_screen__scores",
                value = "Scores"
            ),
            Translation(
                key = "main_menu_screen__achievements",
                value = "Achievements"
            ),
            Translation(
                key = "website",
                value = "Website"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    MainMenuScreen(
        store = Store(
            initialState = MainMenuState(
                loading = false,
                developerCorner = "Developer Corner",
                support = "Support",
                ongoingGameSession = true,
            )
        )
    )
}
