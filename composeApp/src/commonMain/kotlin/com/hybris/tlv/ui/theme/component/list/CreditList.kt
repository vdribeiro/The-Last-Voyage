package com.hybris.tlv.ui.theme.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.Card
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.credit.model.CreditType
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal inline fun <T> CreditList(
    modifier: Modifier = Modifier,
    credits: List<T> = emptyList(),
    noinline id: (T) -> String = { "" },
    crossinline link: (T) -> String? = { null },
    crossinline type: (T) -> CreditType? = { null },
) {
    val creditsMap = credits.groupBy { type(it) }
    val creators = creditsMap[CreditType.CREATOR].orEmpty()
    val sources = creditsMap[CreditType.SOURCE].orEmpty()
    val musics = creditsMap[CreditType.MUSIC].orEmpty()
    val supporters = creditsMap[CreditType.SUPPORTER].orEmpty()

    val uriHandler = LocalUriHandler.current
    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val creatorsTranslation = remember(key1 = translationVersion) { getTranslation(key = "credit_screen__creators") }
    val sourcesTranslation = remember(key1 = translationVersion) { getTranslation(key = "credit_screen__sources") }
    val musicTranslation = remember(key1 = translationVersion) { getTranslation(key = "credit_screen__music") }
    val supportersTranslation = remember(key1 = translationVersion) { getTranslation(key = "credit_screen__supporters") }

    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        columns = StaggeredGridCells.Adaptive(minSize = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalItemSpacing = 8.dp
    ) {

        // Creators
        if (creators.isNotEmpty()) {
            item(key = CreditType.CREATOR, span = StaggeredGridItemSpan.FullLine) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = creatorsTranslation,
                    style = typography.titleLarge,
                )
            }
            items(items = creators, key = id, span = { StaggeredGridItemSpan.FullLine }) { credit ->
                Text(
                    modifier = Modifier
                        .clickable { link(credit)?.let { uriHandler.openUri(uri = it) } },
                    text = id(credit),
                    style = typography.bodyLarge.copy(
                        color = colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                )
            }
        }

        // Data sources
        if (sources.isNotEmpty()) {
            item(key = CreditType.SOURCE, span = StaggeredGridItemSpan.FullLine) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp),
                    text = sourcesTranslation,
                    style = typography.titleLarge,
                )
            }
            items(items = sources, key = id, span = { StaggeredGridItemSpan.FullLine }) { credit ->
                Text(
                    modifier = Modifier
                        .clickable { link(credit)?.let { uriHandler.openUri(uri = it) } },
                    text = id(credit),
                    style = typography.bodyLarge.copy(
                        color = colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                )
            }
        }

        // Music authors
        if (musics.isNotEmpty()) {
            item(key = CreditType.MUSIC, span = StaggeredGridItemSpan.FullLine) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp),
                    text = musicTranslation,
                    style = typography.titleLarge,
                )
            }
            items(items = musics, key = id, span = { StaggeredGridItemSpan.FullLine }) { credit ->
                Text(
                    modifier = Modifier
                        .clickable { link(credit)?.let { uriHandler.openUri(uri = it) } },
                    text = id(credit),
                    style = typography.bodyLarge.copy(
                        color = colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                )
            }
        }

        // Supporters
        if (supporters.isNotEmpty()) {
            item(key = CreditType.SUPPORTER, span = StaggeredGridItemSpan.FullLine) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp),
                    text = supportersTranslation,
                    style = typography.titleLarge,
                )
            }
            items(items = supporters) { credit ->
                Card(
                    modifier = Modifier
                        .clickable { link(credit)?.let { uriHandler.openUri(uri = it) } }
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        text = id(credit),
                        textAlign = TextAlign.Center,
                        style = typography.bodyLarge.copy(
                            color = colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                    )
                }
            }
        }
    }
}