package com.hybris.tlv.ui.screen.stellarexplorer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.hybris.tlv.ui.screen.stellarexplorer.content.StellarHostDetailContent
import com.hybris.tlv.ui.screen.stellarexplorer.content.StellarHostListContent
import com.hybris.tlv.ui.store.Store

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun StellarExplorerScreen(store: Store<StellarExplorerAction, StellarExplorerState>) {
    val storeState by store.stateFlow.collectAsState()

    BackHandler(enabled = true) { store.send(action = StellarExplorerAction.Back) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (storeState.currentContent) {
                null -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Content.LIST -> StellarHostListContent(store = store)
                Content.DETAIL -> StellarHostDetailContent(store = store)
            }
        }
    }
}
