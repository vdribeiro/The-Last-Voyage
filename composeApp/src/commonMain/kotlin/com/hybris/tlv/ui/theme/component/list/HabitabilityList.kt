package com.hybris.tlv.ui.theme.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal inline fun <T> HabitabilityList(
    modifier: Modifier = Modifier,
    properties: List<T> = emptyList(),
    noinline id: (T) -> String = { "" },
    crossinline description: (T) -> String? = { null },
    formula: String = "",
) {
    val uriHandler = LocalUriHandler.current
    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val formulaTranslation = remember(key1 = translationVersion) { getTranslation(key = "formula") }

    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    LazyColumnWithScrollBar(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = properties, key = id) { property ->
            PropertyCard(
                name = getTranslation(key = id(property)),
                description = description(property)?.let { getTranslation(key = it) }
            )
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { uriHandler.openUri(uri = formula) },
                text = formulaTranslation,
                style = typography.headlineSmall.copy(
                    color = colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
            )
        }
    }
}