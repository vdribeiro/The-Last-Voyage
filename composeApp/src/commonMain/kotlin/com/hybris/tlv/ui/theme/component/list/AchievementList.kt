package com.hybris.tlv.ui.theme.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.security.generateUuid
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.AchievementCard
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
    val translationVersion by TranslationCache.stateFlow.collectAsState()
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
                AchievementCard(
                    name = getTranslation(key = id(achievement)),
                    description = description(achievement)?.let { getTranslation(key = it) },
                    done = done(achievement)
                )
            }
        }
    }
}
