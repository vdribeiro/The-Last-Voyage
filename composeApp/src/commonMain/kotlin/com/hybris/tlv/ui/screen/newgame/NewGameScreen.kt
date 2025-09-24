package com.hybris.tlv.ui.screen.newgame

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.hybris.tlv.ui.screen.newgame.content.AdvancedContent
import com.hybris.tlv.ui.screen.newgame.content.NewGameContent
import com.hybris.tlv.ui.screen.newgame.content.StartContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.DebouncedLinearProgressIndicator
import com.hybris.tlv.ui.theme.thenIf

@Composable
internal fun NewGameScreen(store: Store<NewGameAction, NewGameState>) {
    val storeState by store.stateFlow.collectAsState()

    Scaffold(
        modifier = Modifier.thenIf(
            maxWidth = Dp.Infinity,
            maxHeight = Dp.Infinity,
        )
    ) { innerPadding ->
        Box(modifier = Modifier.thenIf(padding = innerPadding)) {
            when (storeState.loading) {
                true -> DebouncedLinearProgressIndicator(
                    modifier = Modifier.thenIf(
                        tag = NEW_GAME_SCREEN_PROGRESS_INDICATOR,
                        maxWidth = Dp.Infinity
                    )
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
