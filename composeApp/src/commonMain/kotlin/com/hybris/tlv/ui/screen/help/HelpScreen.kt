package com.hybris.tlv.ui.screen.help

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.bottombar.HelpBar
import com.hybris.tlv.ui.theme.component.container.HostDefinition
import com.hybris.tlv.ui.theme.component.container.LearnMenu
import com.hybris.tlv.ui.theme.component.container.NavigationHelp
import com.hybris.tlv.ui.theme.component.container.PlanetDefinition
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.list.HabitabilityList
import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.space.formula.spectralTypeToImage
import com.hybris.tlv.usecase.space.formula.toImage
import com.hybris.tlv.usecase.space.model.PlanetType
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
        bottomBar = { HelpBar() }
    ) {
        when (currentContent) {
            Content.LEARN_MENU -> LearnMenu(
                onNavigationClick = { store.send(action = HelpAction.Navigation) },
                onHostDefinitionClick = { store.send(action = HelpAction.HostDefinition) },
                onPlanetDefinitionClick = { store.send(action = HelpAction.PlanetDefinition) },
                onHabitabilityClick = { store.send(action = HelpAction.Habitability) },
                onMechanicsClick = { store.send(action = HelpAction.Mechanics) }
            )

            Content.NAVIGATION -> NavigationHelp()
            Content.HOST_DEFINITION -> {
                val stellarHost = storeState.stellarHost
                HostDefinition(
                    name = stellarHost.name,
                    systemName = stellarHost.systemName,
                    planetCount = stellarHost.planets.size,
                    spectralType = stellarHost.spectralType,
                    spectralImage = stellarHost.spectralType.spectralTypeToImage(),
                    effectiveTemperature = stellarHost.effectiveTemperature,
                    radius = stellarHost.radius,
                    mass = stellarHost.mass,
                    metallicity = stellarHost.metallicity,
                    luminosity = stellarHost.luminosity,
                    gravity = stellarHost.gravity,
                    age = stellarHost.age,
                    density = stellarHost.density,
                    rotationalVelocity = stellarHost.rotationalVelocity,
                    rotationalPeriod = stellarHost.rotationalPeriod,
                    distance = stellarHost.distance,
                    ra = stellarHost.ra,
                    dec = stellarHost.dec,
                    properties = storeState.learningsMap[LearningType.HOST_PROPERTY].orEmpty(),
                    propertyId = { it.id },
                    propertyDescription = { it.description },
                    stellarHosts = storeState.learningsMap[LearningType.HOST_TYPE].orEmpty(),
                    stellarHostId = { it.id },
                    stellarHostDescription = { it.description },
                    stellarHostImage = { it.image.spectralTypeToImage() },
                )
            }

            Content.PLANET_DEFINITION -> {
                val planet = storeState.planet
                PlanetDefinition(
                    name = planet.name,
                    status = planet.status.displayName,
                    orbitalPeriod = planet.orbitalPeriod,
                    orbitAxis = planet.orbitAxis,
                    radius = planet.radius,
                    mass = planet.mass,
                    density = planet.density,
                    eccentricity = planet.eccentricity,
                    insolationFlux = planet.insolationFlux,
                    equilibriumTemperature = planet.equilibriumTemperature,
                    occultationDepth = planet.occultationDepth,
                    inclination = planet.inclination,
                    obliquity = planet.obliquity,
                    type = planet.score?.planetType?.displayName,
                    image = planet.score?.planetType.toImage(),
                    properties = storeState.learningsMap[LearningType.PLANET_PROPERTY].orEmpty(),
                    propertyId = { it.id },
                    propertyDescription = { it.description },
                    planets = storeState.learningsMap[LearningType.PLANET_TYPE].orEmpty(),
                    planetId = { it.id },
                    planetDescription = { it.description },
                    planetImage = { PlanetType.fromValue(value = it.image.orEmpty()).toImage() }
                )
            }

            Content.HABITABILITY -> HabitabilityList(
                properties = storeState.learningsMap[LearningType.FORMULA].orEmpty(),
                id = { it.id },
                description = { it.description },
                formula = storeState.formula
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
                key = "app_name",
                value = "The Last Voyage"
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
                key = "main_menu_screen__definition_types",
                value = "Types"
            ),
            Translation(
                key = "main_menu_screen__planet_definition",
                value = "Planet Definition"
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
