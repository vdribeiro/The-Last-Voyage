package com.hybris.tlv.ui.screen.credit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.credit.model.CreditType
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun CreditScreen(store: Store<CreditState, Unit>) {
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
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .testTag(tag = CREDIT_SCREEN_LIST)
                .fillMaxSize()
                .padding(all = 16.dp),
            columns = StaggeredGridCells.Adaptive(minSize = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalItemSpacing = 8.dp
        ) {
            val creditsMap = storeState.credits.groupBy { it.type }

            // Creators
            val creators = creditsMap[CreditType.CREATOR].orEmpty()
            if (creators.isNotEmpty()) {
                item(key = CreditType.CREATOR, span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_CREATOR)
                            .padding(bottom = 8.dp),
                        text = creatorsTranslation,
                        style = typography.titleLarge,
                    )
                }
                items(items = creators, key = { it.id }, span = { StaggeredGridItemSpan.FullLine }) { credit ->
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_CREATOR_ITEM)
                            .clickable { credit.link?.let { uriHandler.openUri(uri = it) } },
                        text = credit.id,
                        style = typography.bodyLarge.copy(
                            color = colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                    )
                }
            }

            // Data sources
            val sources = creditsMap[CreditType.SOURCE].orEmpty()
            if (sources.isNotEmpty()) {
                item(key = CreditType.SOURCE, span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_SOURCE)
                            .padding(top = 16.dp, bottom = 8.dp),
                        text = sourcesTranslation,
                        style = typography.titleLarge,
                    )
                }
                items(items = sources, key = { it.id }, span = { StaggeredGridItemSpan.FullLine }) { credit ->
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_SOURCE_ITEM)
                            .clickable { credit.link?.let { uriHandler.openUri(uri = it) } },
                        text = credit.id,
                        style = typography.bodyLarge.copy(
                            color = colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                    )
                }
            }

            // Music authors
            val musics = creditsMap[CreditType.MUSIC].orEmpty()
            if (musics.isNotEmpty()) {
                item(key = CreditType.MUSIC, span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_MUSIC)
                            .padding(top = 16.dp, bottom = 8.dp),
                        text = musicTranslation,
                        style = typography.titleLarge,
                    )
                }
                items(items = musics, key = { it.id }, span = { StaggeredGridItemSpan.FullLine }) { credit ->
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_MUSIC_ITEM)
                            .clickable { credit.link?.let { uriHandler.openUri(uri = it) } },
                        text = credit.id,
                        style = typography.bodyLarge.copy(
                            color = colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                    )
                }
            }

            // Supporters
            val supporters = creditsMap[CreditType.SUPPORTER].orEmpty()
            if (supporters.isNotEmpty()) {
                item(key = CreditType.SUPPORTER, span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        modifier = Modifier
                            .testTag(tag = CREDIT_SCREEN_LIST_SUPPORTER)
                            .padding(top = 16.dp, bottom = 8.dp),
                        text = supportersTranslation,
                        style = typography.titleLarge,
                    )
                }
                items(items = supporters) { credit ->
                    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                        Text(
                            modifier = Modifier
                                .testTag(tag = CREDIT_SCREEN_LIST_SUPPORTER_ITEM)
                                .fillMaxWidth()
                                .clickable { credit.link?.let { uriHandler.openUri(uri = it) } }
                                .padding(all = 16.dp),
                            text = credit.id,
                            style = typography.bodyLarge.copy(
                                color = colorScheme.primary,
                                textDecoration = TextDecoration.Underline
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreditLoading() = AppTheme {
    CreditScreen(
        store = getStore(
            initialState = CreditState(
                loading = true,
                credits = emptyList()
            )
        )
    )
}

@Preview
@Composable
private fun CreditList() = AppTheme {
    CreditScreen(
        store = getStore(
            initialState = CreditState(
                loading = false,
                credits = listOf(
                    Credit(
                        id = "engsoneca",
                        link = "https://ko-fi.com/engsoneca",
                        type = CreditType.CREATOR,
                    ),
                    Credit(
                        id = "NASA Exoplanet Archive DOIs 10.26133/NEA13 and 10.26133/NEA40",
                        link = "https://exoplanetarchive.ipac.caltech.edu/",
                        type = CreditType.SOURCE,
                    ),
                    Credit(
                        id = "OpenGameArt",
                        link = "https://opengameart.org/",
                        type = CreditType.MUSIC,
                    ),
                    Credit(
                        id = "You",
                        link = null,
                        type = CreditType.SUPPORTER,
                    ),
                    Credit(
                        id = "Yourself",
                        link = null,
                        type = CreditType.SUPPORTER,
                    ),
                    Credit(
                        id = "Irene",
                        link = null,
                        type = CreditType.SUPPORTER,
                    ),
                    Credit(
                        id = "Jim",
                        link = null,
                        type = CreditType.SUPPORTER,
                    ),
                )
            )
        )
    )
}
