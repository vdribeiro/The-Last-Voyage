package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.ui.screen.game.Content
import com.hybris.tlv.ui.screen.game.GameScreen
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

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
