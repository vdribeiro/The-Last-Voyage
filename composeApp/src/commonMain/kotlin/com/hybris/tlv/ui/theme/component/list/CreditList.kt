package com.hybris.tlv.ui.theme.component.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.translation.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.navigation.open
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.Card
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun <T> CreditList(
    modifier: Modifier = Modifier,
    creators: ImmutableList<T> = persistentListOf(),
    sources: ImmutableList<T> = persistentListOf(),
    musics: ImmutableList<T> = persistentListOf(),
    supporters: ImmutableList<T> = persistentListOf(),
    id: (T) -> String = { it.hashCode().toString() },
    link: (T) -> String? = { null },
) {
    val uriHandler = LocalUriHandler.current
    val creatorsTranslation = getTranslation(key = "credit_screen__creators")
    val sourcesTranslation = getTranslation(key = "credit_screen__sources")
    val musicTranslation = getTranslation(key = "credit_screen__music")
    val supportersTranslation = getTranslation(key = "credit_screen__supporters")

    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    LazyVerticalStaggeredGrid(
        modifier = modifier.testTag(tag = "credits_grid"),
        columns = StaggeredGridCells.Adaptive(minSize = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalItemSpacing = 8.dp
    ) {

        // Creators
        if (creators.isNotEmpty()) {
            item(span = StaggeredGridItemSpan.FullLine) {
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
                        .clickable { link(credit)?.let { uriHandler.open(uri = it) } },
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
            item(span = StaggeredGridItemSpan.FullLine) {
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
                        .clickable { link(credit)?.let { uriHandler.open(uri = it) } },
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
            item(span = StaggeredGridItemSpan.FullLine) {
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
                        .clickable { link(credit)?.let { uriHandler.open(uri = it) } },
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
            item(span = StaggeredGridItemSpan.FullLine) {
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
                        .clickable { link(credit)?.let { uriHandler.open(uri = it) } }
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

@Preview
@Composable
private fun CreditListPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "credit_screen__creators",
                value = "Creators"
            ),
            Translation(
                key = "credit_screen__sources",
                value = "Sources"
            ),
            Translation(
                key = "credit_screen__music",
                value = "Music"
            ),
            Translation(
                key = "credit_screen__supporters",
                value = "Supporters"
            )
        )
    )
    CreditList(
        creators = persistentListOf(
            "Creator 1",
            "Creator 2",
        ),
        sources = persistentListOf(
            "Source 1",
            "Source 2",
        ),
        musics = persistentListOf(
            "Music 1",
            "Music 2",
        ),
        supporters = persistentListOf(
            "Supporter 1",
            "Supporter with a very long name",
            "Supporter lala",
            "Carl Sagan",
            "Johannes Kepler",
            "Isaac Newton",
            "Edwin Hubble",
            "Another supporter with a very long name",
            "Galileo Galilei",
            "Stephen Hawking",
            "Albert Einstein",
        ),
        id = { it },
        link = { "link" },
    )
}
