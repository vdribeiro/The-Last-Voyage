package com.hybris.tlv.ui.theme.component.list

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
import com.hybris.tlv.core.security.uuid
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal inline fun <T> CatastropheList(
    modifier: Modifier = Modifier,
    catastrophes: List<T> = emptyList(),
    noinline id: (T) -> String = { uuid() },
    crossinline description: (T) -> String? = { null }
) {
    val titleTranslation = getTranslation(key = "catastrophe_screen__title")

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
                PropertyCard(
                    name = id(catastrophe),
                    description = description(catastrophe)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CatastropheListPreview() = AppTheme {
    CatastropheList(
        catastrophes = listOf(
            "Catastrophe 1",
            "Catastrophe 2",
            "Catastrophe 3",
        ),
        id = { it },
        description = { it },
    )
}
