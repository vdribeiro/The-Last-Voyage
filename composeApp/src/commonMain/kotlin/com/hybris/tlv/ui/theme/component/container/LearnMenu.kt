package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun LearnMenu(
    modifier: Modifier = Modifier,
    onNavigationClick: () -> Unit = {},
    onHostDefinitionClick: () -> Unit = {},
    onPlanetDefinitionClick: () -> Unit = {},
    onHabitabilityClick: () -> Unit = {},
    onMechanicsClick: () -> Unit = {}
) {
    val translationVersion by TranslationCache.versionFlow.collectAsState()
    val helpTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__learn") }
    val navigationTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__navigation") }
    val hostDefinitionTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__host_definition") }
    val planetDefinitionTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__planet_definition") }
    val habitabilityTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__habitability") }
    val mechanicsTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__mechanics") }

    val typography = LocalTypography.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        scrollBar = false
    ) {
        item {
            Text(
                modifier = Modifier
                    .padding(all = 16.dp),
                text = helpTranslation,
                style = typography.displaySmall,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { onNavigationClick() },
                text = navigationTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { onHostDefinitionClick() },
                text = hostDefinitionTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { onPlanetDefinitionClick() },
                text = planetDefinitionTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { onHabitabilityClick() },
                text = habitabilityTranslation,
                style = typography.headlineMedium,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .clickable { onMechanicsClick() },
                text = mechanicsTranslation,
                style = typography.headlineMedium,
            )
        }
    }
}

@Preview
@Composable
private fun LearnMenuPreview() = AppTheme {
    LearnMenu()
}
