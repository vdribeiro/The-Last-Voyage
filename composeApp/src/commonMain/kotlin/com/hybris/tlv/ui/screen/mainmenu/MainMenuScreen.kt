package com.hybris.tlv.ui.screen.mainmenu

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.hybris.tlv.platform.isAndroid
import com.hybris.tlv.platform.isDesktop
import com.hybris.tlv.platform.isIos
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.bottombar.MainNavigation
import com.hybris.tlv.ui.theme.component.bottombar.Snackbar
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.dialog.Dialog
import com.hybris.tlv.ui.theme.component.image.AppLogo
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun MainMenuScreen(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsState()
    val showNavigationInfo = storeState.showNavigationInfo

    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val appNameTranslation = remember(key1 = translationVersion) { getTranslation(key = "app_name") }
    val tutorialTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__new_game_tutorial") }
    val newGameTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__new_game") }
    val continueTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__continue") }
    val stellarExplorerTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__stellar_explorer") }
    val scoresTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__scores") }
    val achievementsTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__achievements") }

    val typography = LocalTypography.current

    Screen(
        modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN),
        loading = storeState.loading,
        newVersionBanner = storeState.newVersionBanner,
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            MainNavigation(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_BOTTOM_BAR),
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

        LazyColumn(
            modifier = Modifier
                .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT)
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                AppLogo(
                    modifier = Modifier.padding(bottom = 16.dp),
                    showBackground = false,
                    text = appNameTranslation
                )
            }
            if (storeState.featureNewGame) {
                item {
                    Text(
                        modifier = Modifier
                            .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_NEW_GAME)
                            .clickable { store.send(action = MainMenuAction.NewGame) },
                        text = newGameTranslation,
                        style = typography.headlineMedium,
                    )
                }
                if (storeState.ongoingGameSession) {
                    item {
                        Text(
                            modifier = Modifier
                                .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_CONTINUE)
                                .clickable { store.send(action = MainMenuAction.Next) },
                            text = continueTranslation,
                            style = typography.headlineMedium,
                        )
                    }
                }
            }
            if (storeState.featureStellarExplorer) {
                item {
                    Text(
                        modifier = Modifier
                            .testTag(tag = MAIN_MENU_SCREEN_MAIN_CONTENT_STELLAR_EXPLORER)
                            .clickable { store.send(action = MainMenuAction.StellarExplorer) },
                        text = stellarExplorerTranslation,
                        style = typography.headlineMedium,
                    )
                }
            }
            if (storeState.featureScores) {
                item {
                    Text(
                        modifier = Modifier
                            .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SCORES)
                            .clickable { store.send(action = MainMenuAction.Scores) },
                        text = scoresTranslation,
                        style = typography.headlineMedium,
                    )
                }
            }
            if (storeState.featureAchievements) {
                item {
                    Text(
                        modifier = Modifier
                            .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_ACHIEVEMENTS)
                            .clickable { store.send(action = MainMenuAction.Achievements) },
                        text = achievementsTranslation,
                        style = typography.headlineMedium,
                    )
                }
            }
        }
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
