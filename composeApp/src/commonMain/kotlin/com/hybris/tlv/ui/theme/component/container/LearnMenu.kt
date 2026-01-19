package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.card.Card
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.translation.TranslationCache
import com.hybris.tlv.ui.translation.getTranslation

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
            item {
                Card(modifier = Modifier.clickable { onNavigationClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = navigationTranslation,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }
            item {
                Card(modifier = Modifier.clickable { onPanelClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = panelTranslation,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }
            item {
                Card(modifier = Modifier.clickable { onMechanicsClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = mechanicsTranslation,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }
            item {
                Card(modifier = Modifier.clickable { onHostDefinitionClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = hostDefinitionTranslation,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }
            item {
                Card(modifier = Modifier.clickable { onHostTypesClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = hostTypesTranslation,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }
            item {
                Card(modifier = Modifier.clickable { onPlanetDefinitionClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = planetDefinitionTranslation,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }
            item {
                Card(modifier = Modifier.clickable { onPlanetTypesClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = planetTypesTranslation,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }
            item {
                Card(modifier = Modifier.clickable { onHabitabilityClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = habitabilityTranslation,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }
            item {
                Card(modifier = Modifier.clickable { onScoreClick() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = scoreTranslation,
                            style = typography.headlineSmall,
                        )
                    }
                }
            }
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
        )
    )
    LearnMenu()
}
