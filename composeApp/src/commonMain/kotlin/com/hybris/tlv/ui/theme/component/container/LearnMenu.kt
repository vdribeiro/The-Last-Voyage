package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun LearnMenu(
    modifier: Modifier = Modifier,
    onNavigationClick: () -> Unit = {},
    onMechanicsClick: () -> Unit = {},
    onHostDefinitionClick: () -> Unit = {},
    onHostTypesClick: () -> Unit = {},
    onPlanetDefinitionClick: () -> Unit = {},
    onPlanetTypesClick: () -> Unit = {},
    onHabitabilityClick: () -> Unit = {},
) {
    val helpTranslation = getTranslation(key = "main_menu_screen__learn")
    val navigationTranslation = getTranslation(key = "main_menu_screen__navigation")
    val mechanicsTranslation = getTranslation(key = "main_menu_screen__mechanics")
    val hostDefinitionTranslation = getTranslation(key = "main_menu_screen__host_definition")
    val hostTypesTranslation = getTranslation(key = "main_menu_screen__host_types")
    val planetDefinitionTranslation = getTranslation(key = "main_menu_screen__planet_definition")
    val planetTypesTranslation = getTranslation(key = "main_menu_screen__planet_types")
    val habitabilityTranslation = getTranslation(key = "main_menu_screen__habitability")

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
                    .clickable { onMechanicsClick() },
                text = mechanicsTranslation,
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
                    .clickable { onHostTypesClick() },
                text = hostTypesTranslation,
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
                    .clickable { onPlanetTypesClick() },
                text = planetTypesTranslation,
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
    }
}

@Preview
@Composable
private fun LearnMenuPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__learn",
                value = "Learn"
            ),
            Translation(
                key = "main_menu_screen__navigation",
                value = "Navigation"
            ),
            Translation(
                key = "main_menu_screen__host_definition",
                value = "Host Definition"
            ),
            Translation(
                key = "main_menu_screen__planet_definition",
                value = "Planet Definition"
            ),
            Translation(
                key = "main_menu_screen__host_types",
                value = "Host Types"
            ),
            Translation(
                key = "main_menu_screen__planet_types",
                value = "Planet Types"
            ),
            Translation(
                key = "main_menu_screen__habitability",
                value = "Habitability"
            ),
            Translation(
                key = "main_menu_screen__mechanics",
                value = "Tutorial"
            ),
        )
    )
    LearnMenu()
}
