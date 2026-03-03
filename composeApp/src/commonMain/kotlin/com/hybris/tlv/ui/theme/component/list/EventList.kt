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
import com.hybris.tlv.ui.theme.component.card.EventCard
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun <T> EventList(
    modifier: Modifier = Modifier,
    titleTranslation: String = getTranslation(key = "event_screen__title"),
    events: ImmutableList<T> = persistentListOf(),
    id: (T) -> String = { it.hashCode().toString() },
    parentId: (T) -> String? = { null },
    description: (T) -> String? = { null },
    outcome: (T) -> String? = { null }
) {
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
                EventCard(
                    name = id(event),
                    description = description(event),
                    parent = parentId(event),
                    outcome = outcome(event)
                )
            }
        }
    }
}

@Preview
@Composable
private fun EventListPreview() = Preview {
    EventList(
        events = persistentListOf(
            "Event 1",
            "Event 2",
            "Event 3",
        ),
        id = { it },
        parentId = { it },
        description = { it },
        outcome = { it }
    )
}
