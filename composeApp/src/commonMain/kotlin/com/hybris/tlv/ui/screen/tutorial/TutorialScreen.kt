package com.hybris.tlv.ui.screen.tutorial

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.bottombar.GameNavigationBar
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.container.ShipStats
import com.hybris.tlv.ui.theme.component.image.Icon
import com.hybris.tlv.ui.theme.component.list.SystemList
import com.hybris.tlv.ui.theme.component.list.TravelList
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.component.text.TitleDescription
import com.hybris.tlv.ui.theme.component.topbar.StatusBar
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.spectralTypeToImage
import com.hybris.tlv.usecase.space.toImage
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun TutorialScreen(store: Store<TutorialState, TutorialAction>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = storeState.ship
    val currentContent = storeState.currentContent

    val typography = LocalTypography.current

    Screen(
        title = {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = getTranslation(key = "main_menu_screen__mechanics"),
                style = typography.labelLarge,
            )
        },
        onBackClick = { store.back() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp
                ),
                hull = ship.integrity.toString(),
                fuel = ship.fuel.toString(),
                materials = ship.materials.toString(),
                cryopods = ship.cryopods.toString()
            )
        },
        bottomBar = {
            when (currentContent) {
                Content.WELCOME -> Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = getTranslation(key = "tutorial_screen__mechanics_welcome_next"),
                    onClick = { store.send(action = TutorialAction.Next) }
                )

                Content.GOAL -> {}
                Content.SHIP -> {}
                Content.TRAVEL -> Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = getTranslation(key = "tutorial_screen__mechanics_travel_description"),
                    style = typography.bodyLarge
                )

                Content.SYSTEM -> Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = getTranslation(key = "tutorial_screen__mechanics_system_description"),
                    style = typography.bodyLarge
                )

                Content.GAME_OVER -> Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = getTranslation(key = "tutorial_screen__mechanics_game_over_next"),
                    onClick = { store.send(action = TutorialAction.Next) }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = getTranslation(
                        key = when (currentContent) {
                            Content.WELCOME -> "tutorial_screen__mechanics_welcome_next"
                            Content.GOAL -> "tutorial_screen__mechanics_goal_next"
                            Content.SHIP -> "tutorial_screen__mechanics_attributes_next"
                            Content.TRAVEL -> "tutorial_screen__mechanics_travel_next"
                            Content.SYSTEM -> "tutorial_screen__mechanics_system_next"
                            Content.GAME_OVER -> ""
                        }
                    ),
                    style = typography.bodyLarge
                )
                if (currentContent != Content.GAME_OVER) Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "Next"
                )
            }
            GameNavigationBar(
                shipEnabled = currentContent == Content.GOAL,
                shipSelected = currentContent == Content.SHIP,
                shipOnClick = { store.send(action = TutorialAction.Next) },
                systemEnabled = currentContent == Content.TRAVEL,
                systemSelected = currentContent == Content.SYSTEM,
                systemOnClick = { store.send(action = TutorialAction.Next) },
                travelEnabled = currentContent == Content.SHIP,
                travelSelected = currentContent == Content.TRAVEL,
                travelOnClick = { store.send(action = TutorialAction.Next) }
            )
        }
    ) {
        when (currentContent) {
            Content.WELCOME -> TitleDescription(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 32.dp),
                title = getTranslation(key = "tutorial_screen__mechanics_welcome"),
                description = getTranslation(key = "tutorial_screen__mechanics_welcome_description")
            )

            Content.GOAL -> TitleDescription(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 32.dp),
                title = getTranslation(key = "tutorial_screen__mechanics_goal_title"),
                description = getTranslation(key = "tutorial_screen__mechanics_goal_description")
            )

            Content.SHIP -> ShipStats(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                tutorial = true
            )

            Content.TRAVEL -> TravelList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                stellarHosts = listOf(
                    StellarHost(
                        id = "proxima_centauri",
                        name = "Proxima Centauri",
                        systemName = "Alpha Centauri",
                        spectralType = "M5.5V",
                        effectiveTemperature = 2900.0,
                        radius = 0.141,
                        mass = 0.1221,
                        metallicity = null,
                        luminosity = -2.8,
                        gravity = 5.3201025,
                        age = null,
                        density = 48.7626491,
                        rotationalVelocity = null,
                        rotationalPeriod = 90.0,
                        distance = 4.2439092564,
                        ra = 217.3934657,
                        dec = -62.6761821
                    ),
                ),
                id = { it.id },
                name = { it.name },
                planetCount = { it.planets.size },
                spectralType = { it.spectralType },
                spectralImage = { it.spectralType.spectralTypeToImage() },
                distance = { it.distance },
            )

            Content.SYSTEM -> SystemList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                stellarHostName = "Sol",
                stellarHostSpectralType = "G2V",
                stellarHostSpectralImage = "G2V".spectralTypeToImage(),
                stellarHostEffectiveTemperature = 5778.0,
                stellarHostRadius = 1.0,
                stellarHostMass = 1.0,
                stellarHostAge = 4.6,
                planets = listOf(
                    Planet(
                        id = "mars",
                        name = "Mars",
                        stellarHostId = "sol",
                        status = PlanetStatus.CONFIRMED,
                        orbitalPeriod = 687.0,
                        orbitAxis = 1.524,
                        radius = 0.532,
                        mass = 0.107,
                        density = 3.934,
                        eccentricity = 0.094,
                        insolationFlux = 0.430,
                        equilibriumTemperature = 210.0,
                        occultationDepth = 0.000024,
                        inclination = 1.85,
                        obliquity = 25.2,
                    ),
                ),
                planetId = { it.id },
                planetName = { it.name },
                planetRadius = { it.radius },
                planetMass = { it.mass },
                planetDensity = { it.density },
                planetEquilibriumTemperature = { it.equilibriumTemperature },
                planetHabitability = { it.score?.habitabilityScore },
                planetType = { it.score?.planetType?.displayName },
                planetImage = { it.score?.planetType.toImage() },
                onClick = { store.send(action = TutorialAction.Next) }
            )

            Content.GAME_OVER -> TitleDescription(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 32.dp),
                title = getTranslation(key = "tutorial_screen__mechanics_game_over_title"),
                description = getTranslation(key = "tutorial_screen__mechanics_game_over_description")
            )
        }
    }
}

@Preview
@Composable
private fun TutorialScreenGoalPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__mechanics",
                value = "Tutorial"
            ),
            Translation(
                key = "tutorial_screen__mechanics_goal_title",
                value = "Goal"
            ),
            Translation(
                key = "tutorial_screen__mechanics_goal_description",
                value = "Win the game!"
            ),
        )
    )
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                currentContent = Content.GOAL,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialScreenShipPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "tutorial_screen__mechanics_attributes_title",
                value = "Ship"
            ),
            Translation(
                key = "tutorial_screen__mechanics_attributes_description",
                value = "Your ship is awesome!"
            ),
        )
    )
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                currentContent = Content.SHIP,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialScreenSystemPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "tutorial_screen__mechanics_system_title",
                value = "System"
            ),
            Translation(
                key = "tutorial_screen__mechanics_system_description",
                value = "There are so many!"
            ),
        )
    )
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                currentContent = Content.SYSTEM,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialScreenTravelPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "tutorial_screen__mechanics_travel_title",
                value = "Travel"
            ),
            Translation(
                key = "tutorial_screen__mechanics_travel_description",
                value = "Warp speed, Captain."
            ),
        )
    )
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                currentContent = Content.TRAVEL,
            )
        )
    )
}

@Preview
@Composable
private fun TutorialScreenGameOverPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "tutorial_screen__mechanics_game_over_title",
                value = "Game Over"
            ),
            Translation(
                key = "tutorial_screen__mechanics_game_over_description",
                value = "Game over man! Game over!"
            ),
        )
    )
    TutorialScreen(
        store = Store(
            initialState = TutorialState(
                currentContent = Content.GAME_OVER,
            )
        )
    )
}
