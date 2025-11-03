package com.hybris.tlv.ui.screen.mainmenu

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.hybris.tlv.platform.isAndroid
import com.hybris.tlv.platform.isDesktop
import com.hybris.tlv.platform.isIos
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.bottombar.MainNavigation
import com.hybris.tlv.ui.theme.component.bottombar.Snackbar
import com.hybris.tlv.ui.theme.component.container.MainMenu
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.dialog.Dialog
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun MainMenuScreen(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsState()
    val showNavigationInfo = storeState.showNavigationInfo

    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val tutorialTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__new_game_tutorial") }

    Screen(
        loading = storeState.loading,
        banner = if (storeState.newVersionBanner) storeState.developerCorner else null,
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            MainNavigation(
                onCreditsClick = { store.send(action = MainMenuAction.Credits) },
                developerCornerUri = storeState.developerCorner,
                supportUri = storeState.support
            )
        },
        snackbarHost = {
            if (showNavigationInfo && !isAndroid) Snackbar(
                message = getTranslation(
                    key = when {
                        isDesktop -> "main_menu_screen__navigation_info_desktop"
                        isIos -> "main_menu_screen__navigation_info_mobile"
                        else -> "main_menu_screen__navigation_info"
                    }
                ),
                buttonText = getTranslation(key = "main_menu_screen__navigation_info_button"),
                onDismiss = { store.send(action = MainMenuAction.HideNavigationInfo) }
            )
        }
    ) {
        if (storeState.newGameDialog) {
            Dialog(
                title = tutorialTranslation,
                onConfirm = { store.send(action = MainMenuAction.YesNewGameDialog) },
                onDismiss = { store.send(action = MainMenuAction.NoNewGameDialog) },
                onDismissRequest = { store.send(action = MainMenuAction.HideNewGameDialog) },
            )
        }
        MainMenu(
            featureScores = storeState.featureNewGame,
            onScoresClick = { store.send(action = MainMenuAction.Scores) },
            featureAchievements = storeState.featureAchievements,
            onAchievementsClick = { store.send(action = MainMenuAction.Achievements) },
            featureStellarExplorer = storeState.featureStellarExplorer,
            onStellarExplorerClick = { store.send(action = MainMenuAction.StellarExplorer) },
            featureNewGame = storeState.featureNewGame,
            onNewGameClick = { store.send(action = MainMenuAction.NewGame) },
            ongoingGameSession = storeState.ongoingGameSession,
            onOngoingGameSessionClick = { store.send(action = MainMenuAction.Next) }
        )
    }
}

@Preview
@Composable
private fun MainMenuLoadingPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    MainMenuScreen(
        store = getStore(
            initialState = MainMenuState(
                loading = true,
                featureScores = true,
                featureAchievements = true,
                featureStellarExplorer = true,
                featureNewGame = true,
                developerCorner = "Developer Corner",
                support = "Support",
                ongoingGameSession = false,
                newGameDialog = false
            )
        )
    )
}

@Preview
@Composable
private fun MainMenuAllPreview() = AppTheme {
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
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    MainMenuScreen(
        store = getStore(
            initialState = MainMenuState(
                loading = false,
                featureScores = true,
                featureAchievements = true,
                featureStellarExplorer = true,
                featureNewGame = true,
                developerCorner = "Developer Corner",
                support = "Support",
                ongoingGameSession = false,
                newGameDialog = false
            )
        )
    )
}

@Preview
@Composable
private fun MainMenuContinuePreview() = AppTheme {
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
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    MainMenuScreen(
        store = getStore(
            initialState = MainMenuState(
                loading = false,
                featureScores = true,
                featureAchievements = true,
                featureStellarExplorer = true,
                featureNewGame = true,
                developerCorner = "Developer Corner",
                support = "Support",
                ongoingGameSession = true,
                newGameDialog = false
            )
        )
    )
}

@Preview
@Composable
private fun MainMenuNoFeaturesPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "app_name",
                value = "The Last Voyage"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    MainMenuScreen(
        store = getStore(
            initialState = MainMenuState(
                loading = false,
                featureScores = false,
                featureAchievements = false,
                featureStellarExplorer = false,
                featureNewGame = false,
                developerCorner = "Developer Corner",
                support = "Support",
                ongoingGameSession = false,
                newGameDialog = false
            )
        )
    )
}
