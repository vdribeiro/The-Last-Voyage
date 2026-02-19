package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.Card
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun LearnMenu(
    modifier: Modifier = Modifier,
    onNavigationClick: () -> Unit = {},
    onPanelClick: () -> Unit = {},
    onMechanicsClick: () -> Unit = {},
    onHostDefinitionClick: () -> Unit = {},
    onHostTypesClick: () -> Unit = {},
    onPlanetDefinitionClick: () -> Unit = {},
    onPlanetTypesClick: () -> Unit = {},
    onHabitabilityClick: () -> Unit = {},
    onScoreClick: () -> Unit = {},
    onCatastrophesClick: () -> Unit = {},
    onEventsClick: () -> Unit = {},
) {
    val helpTranslation = getTranslation(key = "main_menu_screen__learn")
    val navigationTranslation = getTranslation(key = "main_menu_screen__navigation")
    val panelTranslation = getTranslation(key = "main_menu_screen__control_panel")
    val mechanicsTranslation = getTranslation(key = "main_menu_screen__mechanics")
    val hostDefinitionTranslation = getTranslation(key = "main_menu_screen__host_definition")
    val hostTypesTranslation = getTranslation(key = "main_menu_screen__host_types")
    val planetDefinitionTranslation = getTranslation(key = "main_menu_screen__planet_definition")
    val planetTypesTranslation = getTranslation(key = "main_menu_screen__planet_types")
    val habitabilityTranslation = getTranslation(key = "main_menu_screen__habitability")
    val scoreTranslation = getTranslation(key = "main_menu_screen__score")
    val catastrophesTranslation = getTranslation(key = "catastrophe_screen__title")
    val eventsTranslation = getTranslation(key = "event_screen__title")

    val typography = LocalTypography.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = helpTranslation,
            style = typography.headlineMedium,
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp),
            scrollBar = false
        ) {
            val menuItem = @Composable { text: String, onClick: () -> Unit ->
                Card(modifier = Modifier.clickable { onClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = text,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }

            item { menuItem(navigationTranslation, onNavigationClick) }
            item { menuItem(panelTranslation, onPanelClick) }
            item { menuItem(mechanicsTranslation, onMechanicsClick) }
            item { menuItem(hostDefinitionTranslation, onHostDefinitionClick) }
            item { menuItem(hostTypesTranslation, onHostTypesClick) }
            item { menuItem(planetDefinitionTranslation, onPlanetDefinitionClick) }
            item { menuItem(planetTypesTranslation, onPlanetTypesClick) }
            item { menuItem(habitabilityTranslation, onHabitabilityClick) }
            item { menuItem(scoreTranslation, onScoreClick) }
            item { menuItem(catastrophesTranslation, onCatastrophesClick) }
            item { menuItem(eventsTranslation, onEventsClick) }
        }
    }
}

@Preview
@Composable
private fun LearnMenuPreview() = AppTheme {
    InjectTranslations(
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
                key = "main_menu_screen__control_panel",
                value = "Control Panel"
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
                key = "main_menu_screen__score",
                value = "Score Formula"
            ),
            Translation(
                key = "main_menu_screen__mechanics",
                value = "Tutorial"
            ),
            Translation(
                key = "catastrophe_screen__title",
                value = "Catastrophes"
            ),
            Translation(
                key = "event_screen__title",
                value = "Events"
            ),
        )
    )
    LearnMenu()
}
