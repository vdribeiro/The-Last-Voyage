package com.hybris.tlv.ui.screen.newgame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.ui.screen.newgame.content.NewGameContent
import com.hybris.tlv.ui.screen.newgame.content.StartContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.Screen

@Composable
internal fun NewGameScreen(store: Store<NewGameState, NewGameAction>) {
    val storeState by store.stateFlow.collectAsState()

    Screen(
        modifier = Modifier.testTag(tag = NEW_GAME_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
    ) {
        when (storeState.currentContent) {
            Content.SHIP -> NewGameContent(store = store)
            Content.START -> StartContent(store = store)
        }
    }
}
