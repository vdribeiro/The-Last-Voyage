package com.hybris.tlv.ui.screen.mainmenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.core.platform.open
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.bottombar.MainBar
import com.hybris.tlv.ui.theme.component.container.MainMenu
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun MainMenuScreen(store: Store<MainMenuState, MainMenuAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        store = store,
        loading = storeState.loading,
        title = if (storeState.newVersionBanner) {
            {
                val uriHandler = LocalUriHandler.current
                val typography = LocalTypography.current
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { uriHandler.open(uri = storeState.developerCorner) },
                    text = getTranslation(key = "new_version"),
                    style = typography.labelLarge,
                )
            }
        } else null,
        onBackClick = null,
        bottomBar = {
            MainBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                onCreditsClick = { store.send(action = MainMenuAction.Credits) },
                developerCornerUri = storeState.developerCorner,
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
            onOngoingGameSessionClick = { store.send(action = MainMenuAction.Game) }
        )
    }
}

@Preview
@Composable
private fun MainMenuScreenLoadingPreview() = Preview {
    InjectTranslations(
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
                ongoingGameSession = false,
            )
        )
    )
}

@Preview
@Composable
private fun MainMenuScreenAllPreview() = Preview {
    InjectTranslations(
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
                ongoingGameSession = false,
            )
        )
    )
}

@Preview
@Composable
private fun MainMenuScreenContinuePreview() = Preview {
    InjectTranslations(
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
                ongoingGameSession = true,
            )
        )
    )
}
