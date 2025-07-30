package com.hybris.tlv.ui.screen.credits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.CreditsText
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.credits.model.CreditsType
import com.hybris.tlv.usecase.translation.getTranslation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun CreditsScreen(store: Store<CreditsAction, CreditsState>) {
    val storeState by store.stateFlow.collectAsState()
    val uriHandler = LocalUriHandler.current

    BackHandler(enabled = true) { store.send(action = CreditsAction.Back) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                val creditsMap = storeState.credits.groupBy { it.type }
                item(key = CreditsType.CREATOR) {
                    Text(
                        text = getTranslation(key = "credits_screen__creators"),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
                items(items = creditsMap[CreditsType.CREATOR].orEmpty(), key = { it.id }) { credits ->
                    CreditsText(uriHandler = uriHandler, credits = credits)
                }
                item(key = CreditsType.SUPPORTER) {
                    Text(
                        text = getTranslation(key = "credits_screen__supporters"),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
                items(items = creditsMap[CreditsType.SUPPORTER].orEmpty(), key = { it.id }) { credits ->
                    CreditsText(uriHandler = uriHandler, credits = credits)
                }
                item(key = CreditsType.SOURCE) {
                    Text(
                        text = getTranslation(key = "credits_screen__sources"),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
                items(items = creditsMap[CreditsType.SOURCE].orEmpty(), key = { it.id }) { credits ->
                    CreditsText(uriHandler = uriHandler, credits = credits)
                }
                item(key = CreditsType.MUSIC) {
                    Text(
                        text = getTranslation(key = "credits_screen__music"),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
                items(items = creditsMap[CreditsType.MUSIC].orEmpty(), key = { it.id }) { credits ->
                    CreditsText(uriHandler = uriHandler, credits = credits)
                }
            }
        }
    }
}
