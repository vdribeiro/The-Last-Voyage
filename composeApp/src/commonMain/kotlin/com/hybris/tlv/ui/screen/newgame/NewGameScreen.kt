package com.hybris.tlv.ui.screen.newgame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.ui.screen.newgame.content.AdvancedContent
import com.hybris.tlv.ui.screen.newgame.content.NewGameContent
import com.hybris.tlv.ui.screen.newgame.content.StartContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.DebouncedLinearProgressIndicator

@Composable
internal fun NewGameScreen(store: Store<NewGameAction, NewGameState>) {
    val storeState by store.stateFlow.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.thenIf(padding = innerPadding)) {
            when (storeState.loading) {
                true -> DebouncedLinearProgressIndicator(
                    modifier = Modifier
                        .testTag(tag = NEW_GAME_SCREEN_PROGRESS_INDICATOR)
                        .fillMaxWidth()
                )

                false -> when (storeState.currentContent) {
                    Content.SHIP -> NewGameContent(store = store)
                    Content.ADVANCED -> AdvancedContent(store = store)
                    Content.START -> StartContent(store = store)
                }
            }
        }
    }
}
