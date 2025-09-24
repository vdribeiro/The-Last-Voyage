package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_MAIN_MENU_CONTENT
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_CONTINUE
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_NEW_GAME
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_PROGRESS_INDICATOR
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SCORES
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SOON
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.AppLogo
import com.hybris.tlv.ui.theme.component.DebouncedLinearProgressIndicator
import com.hybris.tlv.ui.theme.debouncedClickable
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun MainMenuContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val newGameTranslation = remember { getTranslation(key = "main_menu_screen__new_game") }
    val continueTranslation = remember { getTranslation(key = "main_menu_screen__continue") }
    val learnTranslation = remember { getTranslation(key = "main_menu_screen__learn") }
    val scoresTranslation = remember { getTranslation(key = "main_menu_screen__scores") }
    val soonTranslation = remember { getTranslation(key = "main_menu_screen__soon") }

    val typography = LocalTypography.current

    LazyColumn(
        modifier = Modifier
            .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { AppLogo() }
        item { Spacer(modifier = Modifier.height(height = 32.dp)) }
        if (storeState.loading) {
            item {
                DebouncedLinearProgressIndicator(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_PROGRESS_INDICATOR)
                        .fillMaxWidth()
                )
            }
            return@LazyColumn
        }
        if (storeState.featureNewGame) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_NEW_GAME)
                        .debouncedClickable { store.send(action = MainMenuAction.NewGame) },
                    text = newGameTranslation,
                    style = typography.headlineMedium,
                )
            }
            if (storeState.ongoingGameSession) {
                item {
                    Text(
                        modifier = Modifier
                            .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_CONTINUE)
                            .debouncedClickable { store.send(action = MainMenuAction.Continue) },
                        text = continueTranslation,
                        style = typography.headlineMedium,
                    )
                }
            }
        }
        if (storeState.featureLearn) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_LEARN)
                        .debouncedClickable { store.send(action = MainMenuAction.Learn) },
                    text = learnTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
        if (storeState.featureScores) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SCORES)
                        .debouncedClickable { store.send(action = MainMenuAction.Scores) },
                    text = scoresTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
        if (storeState.featureSoon) {
            item {
                Text(
                    modifier = Modifier
                        .testTag(tag = MAIN_MENU_SCREEN_MAIN_MENU_CONTENT_SOON)
                        .debouncedClickable { store.send(action = MainMenuAction.Soon) },
                    text = soonTranslation,
                    style = typography.headlineMedium,
                )
            }
        }
    }
}
