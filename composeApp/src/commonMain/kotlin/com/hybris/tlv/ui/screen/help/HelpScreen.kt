package com.hybris.tlv.ui.screen.help

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hybris.tlv.platform.isAndroid
import com.hybris.tlv.platform.isDesktop
import com.hybris.tlv.platform.isIos
import com.hybris.tlv.platform.open
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.bottombar.HelpBar
import com.hybris.tlv.ui.theme.component.bottombar.Snackbar
import com.hybris.tlv.ui.theme.component.container.LearnMenu
import com.hybris.tlv.ui.theme.component.container.PropertyList
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.spectralTypeToImage
import com.hybris.tlv.usecase.space.toImage
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun HelpScreen(store: Store<HelpState, HelpAction>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            if (currentContent == Content.LEARN_MENU) HelpBar(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { store.send(action = HelpAction.VersionClick(reset = false)) }
                    .padding(horizontal = 16.dp)
            )
        },
        snackbarHost = {
            if (storeState.showSnackbar) {
                val message = getTranslation(
                    key = when {
                        isIos || isAndroid -> "konami_mobile"
                        else -> "konami_desktop"
                    }
                )
                Snackbar(
                    message = message,
                    durationMillis = 5000L,
                    onDismiss = { store.send(action = HelpAction.VersionClick(reset = true)) }
                )
            }
        }
    ) {
        when (currentContent) {
            Content.LEARN_MENU -> LearnMenu(
                onNavigationClick = { store.send(action = HelpAction.Navigation) },
                onHostDefinitionClick = { store.send(action = HelpAction.HostDefinition) },
                onHostTypesClick = { store.send(action = HelpAction.HostType) },
                onPlanetDefinitionClick = { store.send(action = HelpAction.PlanetDefinition) },
                onPlanetTypesClick = { store.send(action = HelpAction.PlanetType) },
                onHabitabilityClick = { store.send(action = HelpAction.Habitability) },
                onMechanicsClick = { store.send(action = HelpAction.Mechanics) }
            )

            Content.NAVIGATION -> PropertyList(
                title = getTranslation(key = "main_menu_screen__navigation"),
                properties = listOf(
                    Learning(
                        id = getTranslation(key = "main_menu_screen__navigation"),
                        description = getTranslation(
                            key = when {
                                isDesktop -> "main_menu_screen__navigation_info_desktop"
                                isIos || isAndroid -> "main_menu_screen__navigation_info_mobile"
                                else -> "main_menu_screen__navigation_info"
                            }
                        ),
                        image = null,
                        type = LearningType.FORMULA // TODO
                    )
                ),
                id = { it.id },
                description = { it.description }
            )

            Content.HOST_DEFINITION -> PropertyList(
                title = getTranslation(key = "main_menu_screen__host_definition"),
                properties = storeState.learningsMap[LearningType.HOST_PROPERTY].orEmpty(),
                id = { it.id },
                description = { it.description },
            )

            Content.HOST_TYPE -> PropertyList(
                title = getTranslation(key = "main_menu_screen__host_types"),
                properties = storeState.learningsMap[LearningType.HOST_TYPE].orEmpty(),
                id = { it.id },
                description = { it.description },
                image = { it.image.spectralTypeToImage() },
            )

            Content.PLANET_DEFINITION -> PropertyList(
                title = getTranslation(key = "main_menu_screen__planet_definition"),
                properties = storeState.learningsMap[LearningType.PLANET_PROPERTY].orEmpty(),
                id = { it.id },
                description = { it.description }
            )

            Content.PLANET_TYPE -> PropertyList(
                title = getTranslation(key = "main_menu_screen__planet_types"),
                properties = storeState.learningsMap[LearningType.PLANET_TYPE].orEmpty(),
                id = { it.id },
                description = { it.description },
                image = { PlanetType.fromValue(value = it.image.orEmpty()).toImage() }
            )

            Content.HABITABILITY -> PropertyList(
                title = getTranslation(key = "main_menu_screen__habitability"),
                properties = storeState.learningsMap[LearningType.FORMULA].orEmpty(),
                id = { it.id },
                description = { it.description },
                footer = {
                    storeState.formula?.let { formula ->
                        val formulaTranslation = getTranslation(key = "formula")

                        val uriHandler = LocalUriHandler.current
                        val typography = LocalTypography.current
                        val colorScheme = LocalColorScheme.current
                        Text(
                            modifier = Modifier.clickable { uriHandler.open(uri = formula) },
                            text = formulaTranslation,
                            style = typography.headlineSmall.copy(
                                color = colorScheme.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun HelpScreenLoadingPreview() = AppTheme {
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = true,
            )
        )
    )
}

@Preview
@Composable
private fun HelpScreenPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__learn",
                value = "Help"
            ),
            Translation(
                key = "main_menu_screen__navigation",
                value = "Navigation"
            ),
            Translation(
                key = "main_menu_screen__host_definition",
                value = "Star Definition"
            ),
            Translation(
                key = "main_menu_screen__definition_example",
                value = "Example"
            ),
            Translation(
                key = "main_menu_screen__definition_properties",
                value = "Properties"
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
                value = "Habitability Formula"
            ),
            Translation(
                key = "main_menu_screen__mechanics",
                value = "Tutorial"
            ),
            Translation(
                key = "main_menu_screen__credits",
                value = "Credits"
            ),
        )
    )
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = false,
                formula = "Formula",
                currentContent = Content.LEARN_MENU,
                learningsMap = emptyMap(),
            )
        )
    )
}

@Preview
@Composable
private fun HelpScreenHostDefinitionPreview() = AppTheme {
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = false,
                formula = "Formula",
                currentContent = Content.HOST_DEFINITION,
                learningsMap = listOf(
                    Learning(
                        id = "Luminosity",
                        description = "Shine on you crazy diamond",
                        image = null,
                        type = LearningType.HOST_PROPERTY
                    ),
                    Learning(
                        id = "G",
                        description = "Our Sun",
                        image = "G",
                        type = LearningType.HOST_TYPE
                    ),
                    Learning(
                        id = "W",
                        description = "Wolf-Rayet",
                        image = "W",
                        type = LearningType.HOST_TYPE
                    ),
                ).groupBy { it.type },
            )
        )
    )
}

@Preview
@Composable
private fun HelpScreenPlanetDefinitionPreview() = AppTheme {
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = false,
                formula = "Formula",
                currentContent = Content.PLANET_DEFINITION,
                learningsMap = listOf(
                    Learning(
                        id = "Mass",
                        description = "Very Biggus",
                        image = null,
                        type = LearningType.PLANET_PROPERTY
                    ),
                    Learning(
                        id = "Mars",
                        description = "The Red Planet",
                        image = "EARTH_LIKE_PLANET",
                        type = LearningType.PLANET_TYPE
                    ),
                    Learning(
                        id = "Mini Neptune",
                        description = "Mini-Me",
                        image = "MINI_NEPTUNE",
                        type = LearningType.PLANET_TYPE
                    ),
                ).groupBy { it.type },
            )
        )
    )
}

@Preview
@Composable
private fun HelpScreenHabitabilityPreview() = AppTheme {
    HelpScreen(
        store = Store(
            initialState = HelpState(
                loading = false,
                formula = "Formula",
                currentContent = Content.HABITABILITY,
                learningsMap = listOf(
                    Learning(
                        id = "Roche",
                        description = "The Roche limit",
                        image = null,
                        type = LearningType.FORMULA
                    ),
                    Learning(
                        id = "CHZ",
                        description = "The Circumstellar Habitable Zone (CHZ) is the region around a star where liquid water could exist on a planet's surface.\n" +
                                "I use the Kopparapu model with a flat plateau of 1.0 across the entire conservative zone and then a smooth down slope through the optimistic zone, as a simple gradient peaked at the center unfairly penalizes planets like Earth, which is perfectly habitable but located near the inner edge of the Sun's conservative zone.\n" +
                                "The host star's temperature is used to calculate the fluxes with the model's coefficients. If it is not available, the Kasting simple luminosity model is used instead but with a smaller weight.",
                        image = null,
                        type = LearningType.FORMULA
                    ),
                ).groupBy { it.type },
            )
        )
    )
}
