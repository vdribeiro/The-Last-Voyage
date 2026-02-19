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
internal inline fun <T> EventList(
    modifier: Modifier = Modifier,
    events: List<T> = emptyList(),
    noinline id: (T) -> String = { uuid() },
    crossinline description: (T) -> String? = { null },
    crossinline parentId: (T) -> String? = { null }
) {
    val titleTranslation = getTranslation(key = "event_screen__title")

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
                .testTag(tag = "event_list")
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            items(items = events, key = id) { event ->
                PropertyCard(
                    name = id(event),
                    description = description(event)
                )
            }
        }
    }
}

@Preview
@Composable
private fun EventListPreview() = AppTheme {
    EventList(
        events = listOf(
            "Event 1",
            "Event 2",
            "Event 3",
        ),
        id = { it },
        description = { it },
    )
}
