package com.hybris.tlv.ui.screen.game

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.screen.game.content.ShipContent
import com.hybris.tlv.ui.screen.game.content.SystemContent
import com.hybris.tlv.ui.screen.game.content.TravelContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.ui.theme.component.StatusBar
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun GameScreen(store: Store<GameState, GameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = storeState.ship

    val travelTranslation = remember { getTranslation(key = "game_screen__travel") }
    val systemTranslation = remember { getTranslation(key = "game_screen__system") }
    val shipTranslation = remember { getTranslation(key = "game_screen__ship") }

    Screen(
        modifier = Modifier.testTag(tag = GAME_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier
                    .testTag(tag = GAME_SCREEN_STATUS_BAR)
                    .statusBarsPadding(),
                hull = ship?.integrity?.toString(),
                fuel = ship?.fuel?.toString(),
                materials = ship?.materials?.toString(),
                cryopods = ship?.cryopods?.toString()
            )
        },
        bottomBar = {
            // Navigation bar for travel, system and ship status
            NavigationBar(
                modifier = Modifier.testTag(tag = GAME_SCREEN_NAVIGATION_BAR)
            ) {
                NavigationBarItem(
                    modifier = Modifier.testTag(tag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SHIP),
                    icon = { Icon(imageVector = Icons.Filled.Rocket, contentDescription = shipTranslation) },
                    label = { Text(text = shipTranslation) },
                    selected = (storeState.currentContent == Content.SHIP),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SHIP)) },
                )
                NavigationBarItem(
                    modifier = Modifier.testTag(tag = GAME_SCREEN_NAVIGATION_BAR_ITEM_SYSTEM),
                    icon = { Icon(imageVector = Icons.Filled.Hub, contentDescription = systemTranslation) },
                    label = { Text(text = systemTranslation) },
                    selected = (storeState.currentContent == Content.SYSTEM),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.SYSTEM)) },
                )
                NavigationBarItem(
                    modifier = Modifier.testTag(tag = GAME_SCREEN_NAVIGATION_BAR_ITEM_TRAVEL),
                    icon = { Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = travelTranslation) },
                    label = { Text(text = travelTranslation) },
                    selected = (storeState.currentContent == Content.TRAVEL),
                    onClick = { store.send(action = GameAction.ChangeTab(content = Content.TRAVEL)) },
                )
            }
        }
    ) {
        when (storeState.currentContent) {
            Content.SHIP -> ShipContent(store = store)
            Content.SYSTEM -> SystemContent(store = store)
            Content.TRAVEL -> TravelContent(store = store)
        }
    }
}

@Preview
@Composable
private fun GameLoading() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "game_screen__ship",
                value = "Ship"
            ),
            Translation(
                key = "game_screen__system",
                value = "System"
            ),
            Translation(
                key = "game_screen__travel",
                value = "Travel"
            ),
        )
    )
    AppTheme {
        GameScreen(
            store = getStore(
                initialState = GameState(
                    loading = true,
                    currentContent = Content.SYSTEM,
                    ship = null,
                    currentStellarHost = null,
                    nearStellarHosts = emptyList(),
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameShip() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "game_screen__ship",
                value = "Ship"
            ),
            Translation(
                key = "game_screen__system",
                value = "System"
            ),
            Translation(
                key = "game_screen__travel",
                value = "Travel"
            ),
        )
    )
    AppTheme {
        GameScreen(
            store = getStore(
                initialState = GameState(
                    loading = false,
                    currentContent = Content.SHIP,
                    ship = Ship(
                        id = "1",
                        assignedPoints = 10,
                        yearsTraveled = 100.0,
                        sensorRange = 5,
                        integrity = 80,
                        fuel = 100,
                        materials = 90,
                        cryopods = 150,
                    ),
                    currentStellarHost = null,
                    nearStellarHosts = emptyList(),
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameSystem() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "game_screen__ship",
                value = "Ship"
            ),
            Translation(
                key = "game_screen__system",
                value = "System"
            ),
            Translation(
                key = "game_screen__travel",
                value = "Travel"
            ),
        )
    )
    AppTheme {
        GameScreen(
            store = getStore(
                initialState = GameState(
                    loading = false,
                    currentContent = Content.SYSTEM,
                    ship = Ship(
                        id = "1",
                        assignedPoints = 10,
                        yearsTraveled = 100.0,
                        sensorRange = 5,
                        integrity = 80,
                        fuel = 100,
                        materials = 90,
                        cryopods = 150,
                    ),
                    currentStellarHost = StellarHost(
                        id = "sol",
                        name = "Sol",
                        systemName = "Sol",
                        spectralType = "G2V",
                        effectiveTemperature = 5778.0,
                        radius = 1.0,
                        mass = 1.0,
                        metallicity = 0.0,
                        luminosity = 1.0,
                        gravity = 1.0,
                        age = 4.6,
                        density = 1.410,
                        rotationalVelocity = 2.0,
                        rotationalPeriod = 25.05,
                        distance = 0.0,
                        ra = 0.0,
                        dec = 0.0
                    ).apply {
                        planets.add(
                            Planet(
                                id = "earth",
                                name = "Earth",
                                stellarHostId = "sol",
                                status = PlanetStatus.CONFIRMED,
                                orbitalPeriod = 365.2,
                                orbitAxis = 1.000,
                                radius = 1.0,
                                mass = 1.0,
                                density = 5.514,
                                eccentricity = 0.017,
                                insolationFlux = 1.000,
                                equilibriumTemperature = 255.0,
                                occultationDepth = 0.000084,
                                inclination = 0.0,
                                obliquity = 23.4,
                            ),
                        )
                    },
                    nearStellarHosts = emptyList(),
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameTravel() {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "game_screen__ship",
                value = "Ship"
            ),
            Translation(
                key = "game_screen__system",
                value = "System"
            ),
            Translation(
                key = "game_screen__travel",
                value = "Travel"
            ),
        )
    )
    AppTheme {
        GameScreen(
            store = getStore(
                initialState = GameState(
                    loading = false,
                    currentContent = Content.TRAVEL,
                    ship = Ship(
                        id = "1",
                        assignedPoints = 10,
                        yearsTraveled = 100.0,
                        sensorRange = 5,
                        integrity = 80,
                        fuel = 100,
                        materials = 90,
                        cryopods = 150,
                    ),
                    currentStellarHost = null,
                    nearStellarHosts = listOf(
                        StellarHost(
                            id = "sol",
                            name = "Sol",
                            systemName = "Sol",
                            spectralType = "G2V",
                            effectiveTemperature = 5778.0,
                            radius = 1.0,
                            mass = 1.0,
                            metallicity = 0.0,
                            luminosity = 1.0,
                            gravity = 1.0,
                            age = 4.6,
                            density = 1.410,
                            rotationalVelocity = 2.0,
                            rotationalPeriod = 25.05,
                            distance = 0.0,
                            ra = 0.0,
                            dec = 0.0
                        )
                    ),
                )
            )
        )
    }
}
