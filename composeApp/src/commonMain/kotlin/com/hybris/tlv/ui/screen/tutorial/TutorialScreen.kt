package com.hybris.tlv.ui.screen.tutorial

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.bottombar.BottomButton
import com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.ui.theme.component.bottombar.GameNavigationBar
import com.hybris.tlv.ui.theme.component.button.Button
import com.hybris.tlv.ui.theme.component.container.ShipStats
import com.hybris.tlv.ui.theme.component.list.SystemList
import com.hybris.tlv.ui.theme.component.list.TravelList
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.ui.theme.component.text.TextIcon
import com.hybris.tlv.ui.theme.component.text.TitleDescription
import com.hybris.tlv.ui.theme.component.topbar.StatusBar
import com.hybris.tlv.ui.theme.getTranslation
import com.hybris.tlv.domain.usecase.space.model.PlanetType
import com.hybris.tlv.domain.usecase.space.spectralTypeToImage
import com.hybris.tlv.domain.usecase.space.toImage
import com.hybris.tlv.domain.usecase.translation.TranslationCache
import com.hybris.tlv.domain.usecase.translation.model.Translation

@Composable
internal fun TutorialScreen(store: Store<TutorialState, TutorialAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val ship = storeState.ship
    val currentContent = storeState.currentContent

    val typography = LocalTypography.current

    Screen(
        store = store,
        help = false,
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
                Content.WELCOME -> ButtonsBar(
                    buttons = listOf(
                        BottomButton(
                            text = getTranslation(key = "tutorial_screen__mechanics_welcome_start"),
                            onClick = { store.send(action = TutorialAction.Next) }
                        ),
                        BottomButton(
                            text = getTranslation(key = "tutorial_screen__mechanics_welcome_skip"),
                            onClick = { store.send(action = TutorialAction.Skip) }
                        )
                    ),
                )

                Content.GOAL -> TextIcon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    text = getTranslation(key = "tutorial_screen__mechanics_goal_next"),
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                )

                Content.SHIP -> TextIcon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    text = getTranslation(key = "tutorial_screen__mechanics_attributes_next"),
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                )

                Content.TRAVEL -> TextIcon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    text = getTranslation(key = "tutorial_screen__mechanics_travel_next"),
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                )

                Content.SYSTEM -> TextIcon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    text = getTranslation(key = "tutorial_screen__mechanics_system_next"),
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                )

                Content.GAME_OVER -> Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = getTranslation(key = "tutorial_screen__mechanics_game_over_next"),
                    onClick = { store.send(action = TutorialAction.Next) }
                )
            }
            GameNavigationBar(
                modifier = Modifier.testTag(tag = "tutorial_navigation_bar"),
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
                    .padding(all = 16.dp),
                title = getTranslation(key = "tutorial_screen__mechanics_welcome"),
                description = getTranslation(key = "tutorial_screen__mechanics_welcome_description")
            )

            Content.GOAL -> TitleDescription(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                title = getTranslation(key = "tutorial_screen__mechanics_goal_title"),
                description = getTranslation(key = "tutorial_screen__mechanics_goal_description")
            )

            Content.SHIP -> ShipStats(
                modifier = Modifier
                    .testTag(tag = "tutorial_ship_stats")
                    .fillMaxSize()
                    .padding(all = 16.dp),
                tutorial = true
            )

            Content.TRAVEL -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                TravelList(
                    modifier = Modifier
                        .testTag(tag = "tutorial_travel_list")
                        .fillMaxWidth(),
                    stellarHosts = listOf("proxima_centauri"),
                    name = { "Proxima Centauri" },
                    planetCount = { 1 },
                    spectralType = { "M5.5V" },
                    spectralImage = { "M5.5V".spectralTypeToImage() },
                    distance = { 4.24 },
                    footer = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 8.dp),
                            text = getTranslation(key = "tutorial_screen__mechanics_travel_description"),
                            style = typography.bodyLarge
                        )
                    }
                )
            }

            Content.SYSTEM -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                SystemList(
                    modifier = Modifier
                        .testTag(tag = "tutorial_system_list")
                        .fillMaxWidth(),
                    stellarHostName = "Sol",
                    stellarHostSpectralType = "G2V",
                    stellarHostSpectralImage = "G2V".spectralTypeToImage(),
                    stellarHostEffectiveTemperature = 5778.0,
                    stellarHostRadius = 1.0,
                    stellarHostMass = 1.0,
                    stellarHostAge = 4.6,
                    planets = listOf("mars"),
                    planetName = { "Mars" },
                    planetRadius = { 0.532 },
                    planetMass = { 0.107 },
                    planetDensity = { 3.934 },
                    planetEquilibriumTemperature = { 210.0 },
                    planetHabitability = { 0.8 },
                    planetType = { PlanetType.EARTH_LIKE_PLANET.displayName },
                    planetImage = { PlanetType.EARTH_LIKE_PLANET.toImage() },
                    onClick = { store.send(action = TutorialAction.Next) },
                    footer = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 8.dp),
                            text = getTranslation(key = "tutorial_screen__mechanics_system_description"),
                            style = typography.bodyLarge
                        )
                    }
                )
            }

            Content.GAME_OVER -> TitleDescription(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
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
