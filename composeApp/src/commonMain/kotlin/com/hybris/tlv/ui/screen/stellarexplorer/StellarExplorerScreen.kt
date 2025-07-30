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
import com.hybris.tlv.ui.component.ControlPanel
import com.hybris.tlv.ui.screen.stellarexplorer.content.StellarHostDetailContent
import com.hybris.tlv.ui.screen.stellarexplorer.content.StellarHostListContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun StellarExplorerScreen(store: Store<StellarExplorerAction, StellarExplorerState>) {
    val storeState by store.stateFlow.collectAsState()

    BackHandler(enabled = true) { store.send(action = StellarExplorerAction.Back) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ControlPanel(
                onSearch = { store.send(action = StellarExplorerAction.Search(search = it)) },
                view = getTranslation(
                    when (storeState.currentContent) {
                        Content.LIST_HOSTS -> ""
                        Content.LIST_PLANETS -> ""
                        Content.DETAIL_HOSTS -> ""
                        Content.DETAIL_PLANETS -> ""
                        null -> ""
                    }
                ),
                onChangeView = { store.send(action = StellarExplorerAction.ChangeView(view = it)) },
                properties =,
                selectedProperty =,
                ascending =,
                onSortChange =,
                sortDirection =,
                onSortDirectionChange =,
                visibleProperties =,
                onVisibilityChange =
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (storeState.currentContent) {
                null -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Content.LIST -> StellarHostListContent(store = store)
                Content.DETAIL -> StellarHostDetailContent(store = store)
            }
        }
    }
}
