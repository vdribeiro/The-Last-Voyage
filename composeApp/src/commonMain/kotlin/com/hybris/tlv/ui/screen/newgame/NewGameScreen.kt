package com.hybris.tlv.ui.screen.newgame

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.ui.screen.newgame.content.AdvancedContent
import com.hybris.tlv.ui.screen.newgame.content.NewGameContent
import com.hybris.tlv.ui.screen.newgame.content.StartContent
import com.hybris.tlv.ui.store.Store

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun NewGameScreen(store: Store<NewGameAction, NewGameState>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent ?: return

    BackHandler(enabled = true) { store.send(action = NewGameAction.Back) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (currentContent) {
                Content.SHIP -> NewGameContent(store = store)
                Content.ADVANCED -> AdvancedContent(store = store)
                Content.START -> StartContent(store = store)
            }
        }
    }
}
