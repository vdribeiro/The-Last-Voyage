package com.hybris.tlv.ui.screen.mainmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.mainmenu.content.HabitabilityContent
import com.hybris.tlv.ui.screen.mainmenu.content.HostDefinitionContent
import com.hybris.tlv.ui.screen.mainmenu.content.LearnContent
import com.hybris.tlv.ui.screen.mainmenu.content.MainMenuContent
import com.hybris.tlv.ui.screen.mainmenu.content.PlanetDefinitionContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.debouncedClickable
import com.hybris.tlv.ui.theme.thenIf
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.kofi

@Composable
internal fun MainMenuScreen(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val uriHandler = LocalUriHandler.current
    val currentContent = storeState.currentContent
    val isMenu = currentContent == Content.MAIN_MENU || currentContent == Content.LEARN_MENU
    val websiteTranslation = remember { getTranslation(key = "website") }
    val creditsTranslation = remember { getTranslation(key = "main_menu_screen__credits") }

    val typography = LocalTypography.current

    Scaffold(
        modifier = Modifier.thenIf(
            tag = MAIN_MENU_SCREEN,
            maxWidth = Dp.Infinity,
            maxHeight = Dp.Infinity
        ),
        topBar = {
            Row(
                modifier = Modifier.thenIf(
                    tag = MAIN_MENU_SCREEN_TOP_BAR,
                    maxWidth = Dp.Infinity,
                ).statusBarsPadding(),
                horizontalArrangement = Arrangement.End
            ) {
                // Feedback button
                if (isMenu && storeState.featureFeedback) {
                    IconButton(
                        modifier = Modifier.thenIf(tag = MAIN_MENU_SCREEN_TOP_BAR_FEEDBACK),
                        onClick = { store.send(action = MainMenuAction.Feedback) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.BugReport,
                            contentDescription = "Feedback"
                        )
                    }
                } else Spacer(
                    modifier = Modifier.thenIf(
                        minWidth = 48.dp,
                        maxWidth = 48.dp,
                        minHeight = 48.dp,
                        maxHeight = 48.dp
                    )
                ) // Reserve space for the feedback button to avoid layout flickering
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier.thenIf(
                    tag = MAIN_MENU_SCREEN_BOTTOM_BAR,
                    maxWidth = Dp.Infinity,
                    padding = PaddingValues(horizontal = 32.dp)
                ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Website, Credits and Ko-fi options
                if (isMenu) {
                    Text(
                        modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically).thenIf(
                            tag = MAIN_MENU_SCREEN_BOTTOM_BAR_WEBSITE,
                            minWidth = 100.dp,
                            maxWidth = 100.dp,
                            minHeight = 100.dp,
                            maxHeight = 100.dp,
                            onClick = { uriHandler.openUri(uri = storeState.developerCorner) }
                        ),
                        text = websiteTranslation,
                        style = typography.titleSmall,
                    )
                    Text(
                        modifier = Modifier
                            .testTag(tag = MAIN_MENU_SCREEN_BOTTOM_BAR_CREDITS)
                            .size(size = 100.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .debouncedClickable { store.send(action = MainMenuAction.Credits) },
                        text = creditsTranslation,
                        style = typography.titleSmall,
                        textAlign = TextAlign.Center
                    )
                    Image(
                        modifier = Modifier
                            .testTag(tag = MAIN_MENU_SCREEN_BOTTOM_BAR_SUPPORT)
                            .size(size = 100.dp)
                            .debouncedClickable { uriHandler.openUri(uri = storeState.support) },
                        painter = painterResource(resource = Res.drawable.kofi),
                        contentDescription = "Support",
                        contentScale = ContentScale.Fit,
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.thenIf(padding = innerPadding)) {
            when (currentContent) {
                Content.MAIN_MENU -> MainMenuContent(store = store)
                Content.LEARN_MENU -> LearnContent(store = store)
                Content.HOST_DEFINITION -> HostDefinitionContent(store = store)
                Content.PLANET_DEFINITION -> PlanetDefinitionContent(store = store)
                Content.HABITABILITY -> HabitabilityContent(store = store)
            }
        }
    }
}
