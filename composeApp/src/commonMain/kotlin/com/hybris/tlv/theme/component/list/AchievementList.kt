package com.hybris.tlv.theme.component.list

import org.jetbrains.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.unit.dp
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.theme.AppTheme
import com.hybris.tlv.theme.LocalTypography
import com.hybris.tlv.theme.component.card.PropertyCard
import com.hybris.tlv.theme.component.text.Text
import com.hybris.tlv.theme.getTranslation

@Composable
internal inline fun <T> AchievementList(
    modifier: Modifier = Modifier,
    achievements: List<T> = emptyList(),
    noinline id: (T) -> String = { generateUuid() },
    crossinline description: (T) -> String? = { null },
    crossinline done: (T) -> Boolean = { false }
) {
    val titleTranslation = getTranslation(key = "achievements_screen__title")

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
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            items(items = achievements, key = id) { achievement ->
                PropertyCard(
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
private fun AchievementListPreview() = AppTheme {
    AchievementList(
        achievements = listOf(
            "Achievement 1",
            "Achievement 2",
            "Achievement 3",
        ),
        id = { it },
        description = { it },
        done = { it != "Achievement 2" }
    )
}
