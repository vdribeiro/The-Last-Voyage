package com.hybris.tlv.ui.screen.credit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.usecase.credit.model.CreditType
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun CreditScreen(store: Store<CreditAction, CreditState>) {
    val storeState by store.stateFlow.collectAsState()
    val uriHandler = LocalUriHandler.current
    val creatorsTranslation = remember { getTranslation(key = "credit_screen__creators") }
    val sourcesTranslation = remember { getTranslation(key = "credit_screen__sources") }
    val musicTranslation = remember { getTranslation(key = "credit_screen__music") }
    val supportersTranslation = remember { getTranslation(key = "credit_screen__supporters") }

    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    Screen(
        modifier = Modifier.testTag(tag = CREDIT_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag(tag = CREDIT_SCREEN_LIST)
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            val creditsMap = storeState.credits.groupBy { it.type }

            // Creators
            val creators = creditsMap[CreditType.CREATOR].orEmpty()
            if (creators.isNotEmpty()) {
                item(key = CreditType.CREATOR) {
                    Text(
                        modifier = Modifier.testTag(tag = CREDIT_SCREEN_LIST_CREATOR),
                        text = creatorsTranslation,
                        style = typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                }
                items(items = creators, key = { it.id }) { credit ->
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_CREATOR_ITEM)
                            .clickable { credit.link?.let { uriHandler.openUri(uri = it) } },
                        text = credit.id,
                        style = typography.bodyLarge.copy(
                            color = colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            // Data sources
            val sources = creditsMap[CreditType.SOURCE].orEmpty()
            if (sources.isNotEmpty()) {
                item(key = CreditType.SOURCE) {
                    Text(
                        modifier = Modifier.testTag(tag = CREDIT_SCREEN_LIST_SOURCE),
                        text = sourcesTranslation,
                        style = typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                }
                items(items = sources, key = { it.id }) { credit ->
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_SOURCE_ITEM)
                            .clickable { credit.link?.let { uriHandler.openUri(uri = it) } },
                        text = credit.id,
                        style = typography.bodyLarge.copy(
                            color = colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            // Music authors
            val musics = creditsMap[CreditType.MUSIC].orEmpty()
            if (musics.isNotEmpty()) {
                item(key = CreditType.MUSIC) {
                    Text(
                        modifier = Modifier.testTag(tag = CREDIT_SCREEN_LIST_MUSIC),
                        text = musicTranslation,
                        style = typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                }
                items(items = musics, key = { it.id }) { credit ->
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_MUSIC_ITEM)
                            .clickable { credit.link?.let { uriHandler.openUri(uri = it) } },
                        text = credit.id,
                        style = typography.bodyLarge.copy(
                            color = colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            // Supporters
            val supporters = creditsMap[CreditType.SUPPORTER].orEmpty()
            if (supporters.isNotEmpty()) {
                item(key = CreditType.SUPPORTER) {
                    Text(
                        modifier = Modifier.testTag(tag = CREDIT_SCREEN_LIST_SUPPORTER),
                        text = supportersTranslation,
                        style = typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    LazyVerticalGrid(
                        modifier = Modifier.heightIn(max = 500.dp),
                        columns = GridCells.Adaptive(minSize = 100.dp),
                        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                    ) {
                        items(items = supporters) { credit ->
                            Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                                Text(
                                    modifier = Modifier
                                        .testTag(tag = CREDIT_SCREEN_LIST_SUPPORTER_ITEM)
                                        .fillMaxWidth()
                                        .clickable { credit.link?.let { uriHandler.openUri(uri = it) } }
                                        .padding(all = 16.dp),
                                    text = credit.id,
                                    textAlign = TextAlign.Center,
                                    style = typography.bodyLarge.copy(
                                        color = colorScheme.primary,
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
