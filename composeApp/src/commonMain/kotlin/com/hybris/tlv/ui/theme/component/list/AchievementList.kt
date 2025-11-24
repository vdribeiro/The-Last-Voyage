package com.hybris.tlv.ui.theme.component.list

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal inline fun <T> AchievementList(
    modifier: Modifier = Modifier,
    achievements: List<T> = emptyList(),
    noinline id: (T) -> String = { generateUuid() },
    crossinline description: (T) -> String? = { null },
    crossinline done: (T) -> Boolean = { false }
) {
    val translationVersion by TranslationCache.versionFlow.collectAsState()
    val titleTranslation = remember(key1 = translationVersion) { getTranslation(key = "achievements_screen__title") }

    val typography = LocalTypography.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(
            text = titleTranslation,
            style = typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            items(items = achievements, key = id) { achievement ->
                PropertyCard(
                    name = getTranslation(key = id(achievement)),
                    description = description(achievement)?.let { getTranslation(key = it) },
                    icon = if (done(achievement)) {
                        { Icon(imageVector = Icons.Filled.Check) }
                    } else {
                        { Icon() }
                    }
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
        description = { it },
        done = { it != "Achievement 2" }
    )
}
