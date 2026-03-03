package com.hybris.tlv.ui.theme.component.list

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.AchievementCard
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun <T> AchievementList(
    modifier: Modifier = Modifier,
    titleTranslation: String = getTranslation(key = "achievements_screen__title"),
    achievements: ImmutableList<T> = persistentListOf(),
    id: (T) -> String = { it.hashCode().toString() },
    description: (T) -> String? = { null },
    done: (T) -> Boolean = { false }
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
                .testTag(tag = "achievement_list")
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            items(items = achievements, key = id) { achievement ->
                AchievementCard(
                    name = id(achievement),
                    description = description(achievement),
                    trailingIcon = if (done(achievement)) Icons.Filled.Check else null
                )
            }
        }
    }
}

@Preview
@Composable
private fun AchievementListPreview() = Preview {
    AchievementList(
        achievements = persistentListOf(
            "Achievement 1",
            "Achievement 2",
            "Achievement 3",
        ),
        id = { it },
        description = { it },
        done = { it != "Achievement 2" }
    )
}
