package com.hybris.tlv.ui.theme.component.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.CatastropheCard
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun <T> CatastropheList(
    modifier: Modifier = Modifier,
    catastrophes: ImmutableList<T> = persistentListOf(),
    id: (T) -> String = { it.hashCode().toString() },
    name: @Composable (T) -> String? = { null },
    description: @Composable (T) -> String? = { null }
) {
    val titleTranslation: String = getTranslation(key = "catastrophe_screen__title")

    val typography = LocalTypography.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = titleTranslation,
            style = typography.headlineMedium,
        )
        LazyColumn(
            modifier = Modifier
                .testTag(tag = "catastrophe_list")
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            items(items = catastrophes, key = id) { catastrophe ->
                CatastropheCard(
                    name = name(catastrophe),
                    description = description(catastrophe)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CatastropheListPreview() = Preview {
    CatastropheList(
        catastrophes = persistentListOf(
            "Catastrophe 1",
            "Catastrophe 2",
            "Catastrophe 3",
        ),
        id = { it },
        name = { it },
        description = { it },
    )
}
