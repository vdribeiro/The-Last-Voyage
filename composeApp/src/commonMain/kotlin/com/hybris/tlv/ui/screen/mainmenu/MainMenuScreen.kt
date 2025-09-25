package com.hybris.tlv.ui.screen.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.ui.screen.mainmenu.content.HabitabilityContent
import com.hybris.tlv.ui.screen.mainmenu.content.HostDefinitionContent
import com.hybris.tlv.ui.screen.mainmenu.content.LearnContent
import com.hybris.tlv.ui.screen.mainmenu.content.MainMenuContent
import com.hybris.tlv.ui.screen.mainmenu.content.PlanetDefinitionContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.BottomBar
import com.hybris.tlv.ui.theme.component.Screen

@Composable
internal fun MainMenuScreen(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent
    val isMenu = currentContent == Content.MAIN_MENU || currentContent == Content.LEARN_MENU

    Screen(
        modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.music() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            if (isMenu) BottomBar(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_BOTTOM_BAR),
                onCreditsClick = { store.send(action = MainMenuAction.Credits) },
                developerCornerUri = storeState.developerCorner,
                supportUri = storeState.support
            )
        }
    ) {
        when (currentContent) {
            Content.MAIN_MENU -> MainMenuContent(store = store)
            Content.LEARN_MENU -> LearnContent(store = store)
            Content.HOST_DEFINITION -> HostDefinitionContent(store = store)
            Content.PLANET_DEFINITION -> PlanetDefinitionContent(store = store)
            Content.HABITABILITY -> HabitabilityContent(store = store)
        }
    }
}
