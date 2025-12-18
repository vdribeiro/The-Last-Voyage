package com.hybris.tlv.ui.screen.game

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.ui.navigation.Command
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.bottombar.GameNavigationBar
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.container.ShipStats
import com.hybris.tlv.ui.theme.component.list.SystemList
import com.hybris.tlv.ui.theme.component.list.TravelList
import com.hybris.tlv.ui.theme.component.topbar.StatusBar
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.spectralTypeToImage
import com.hybris.tlv.usecase.space.toImage
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun GameScreen(store: Store<GameState, GameAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val ship = storeState.ship

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
        onHelpClick = { store.navigate(screen = Screen.Help) },
        onMusicClick = { store.command(command = Command.ToggleAudio) },
        onFeedbackClick = { store.navigate(screen = Screen.Feedback()) },
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp
                ),
                hull = ship?.integrity?.toString(),
                fuel = ship?.fuel?.toString(),
                materials = ship?.materials?.toString(),
                cryopods = ship?.cryopods?.toString()
            )
        },
        bottomBar = {
            GameNavigationBar(
                shipSelected = storeState.currentContent == Content.SHIP,
                shipOnClick = { store.send(action = GameAction.ChangeTab(content = Content.SHIP)) },
                systemSelected = storeState.currentContent == Content.SYSTEM,
                systemOnClick = { store.send(action = GameAction.ChangeTab(content = Content.SYSTEM)) },
                travelSelected = storeState.currentContent == Content.TRAVEL,
                travelOnClick = { store.send(action = GameAction.ChangeTab(content = Content.TRAVEL)) },
            )
        }
    ) {
        when (storeState.currentContent) {
            Content.SHIP -> ShipStats(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                integrity = ship?.integrity,
                fuel = ship?.fuel,
                materials = ship?.materials,
                cryopods = ship?.cryopods,
                sensorRange = ship?.sensorRange,
                yearsTraveled = ship?.yearsTraveled,
                velocity = ship?.engine?.velocity,
                fuelConsumption = ship?.engine?.fuelConsumption,
            )

            Content.SYSTEM -> {
                val stellarHost = storeState.currentStellarHost ?: return@Screen
                SystemList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 16.dp),
                    stellarHostName = stellarHost.name,
                    stellarHostSpectralType = stellarHost.spectralType,
                    stellarHostSpectralImage = stellarHost.spectralType.spectralTypeToImage(),
                    stellarHostEffectiveTemperature = stellarHost.effectiveTemperature,
                    stellarHostRadius = stellarHost.radius,
                    stellarHostMass = stellarHost.mass,
                    stellarHostAge = stellarHost.age,
                    planets = stellarHost.planets,
                    planetId = { it.id },
                    planetName = { it.name },
                    planetRadius = { it.radius },
                    planetMass = { it.mass },
                    planetDensity = { it.density },
                    planetEquilibriumTemperature = { it.equilibriumTemperature },
                    planetHabitability = { it.score?.habitabilityScore },
                    planetType = { it.score?.planetType?.displayName },
                    planetImage = { it.score?.planetType.toImage() },
                    onClick = { store.send(action = GameAction.Settle(planet = it)) }
                )
            }

            Content.TRAVEL -> TravelList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                stellarHosts = storeState.nearStellarHosts,
                id = { it.id },
                name = { it.name },
                planetCount = { it.planets.size },
                spectralType = { it.spectralType },
                spectralImage = { it.spectralType.spectralTypeToImage() },
                distance = { it.distance },
                onClick = { store.send(action = GameAction.Travel(stellarHost = it)) }
            )
        }
    }
}

@Preview
@Composable
private fun GameScreenLoadingPreview() = AppTheme {
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
    GameScreen(
        store = Store(
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

@Preview
@Composable
private fun GameScreenShipPreview() = AppTheme {
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
            Translation(
                key = "ship_years_traveled",
                value = "Years Travelled"
            ),
            Translation(
                key = "ship_speed",
                value = "Speed"
            ),
            Translation(
                key = "ship_integrity",
                value = "Integrity"
            ),
            Translation(
                key = "ship_sensor",
                value = "Sensor Range"
            ),
            Translation(
                key = "ship_fuel",
                value = "Fuel"
            ),
            Translation(
                key = "ship_materials",
                value = "Materials"
            ),
            Translation(
                key = "ship_cryopods",
                value = "Cryopods"
            ),
        )
    )
    GameScreen(
        store = Store(
            initialState = GameState(
                loading = false,
                currentContent = Content.SHIP,
                ship = Ship(
                    id = "1",
                    engine = Engine(
                        id = "1",
                        description = "",
                        velocity = 0.1,
                        fuelConsumption = 0.0,
                        cost = 0
                    ),
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

@Preview
@Composable
private fun GameScreenSystemPreview() = AppTheme {
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
            Translation(
                key = "planet_radius",
                value = "Radius"
            ),
            Translation(
                key = "planet_mass",
                value = "Mass"
            ),
            Translation(
                key = "planet_density",
                value = "Density"
            ),
            Translation(
                key = "planet_temperature",
                value = "Temperature"
            ),
            Translation(
                key = "stellar_host_type",
                value = "Host"
            ),
            Translation(
                key = "stellar_host_temperature",
                value = "Temperature"
            ),
            Translation(
                key = "stellar_host_radius",
                value = "Radius"
            ),
            Translation(
                key = "stellar_host_mass",
                value = "Mass"
            ),
            Translation(
                key = "stellar_host_age",
                value = "Age"
            )
        )
    )
    GameScreen(
        store = Store(
            initialState = GameState(
                loading = false,
                currentContent = Content.SYSTEM,
                ship = Ship(
                    id = "1",
                    engine = Engine(
                        id = "1",
                        description = "",
                        velocity = 0.1,
                        fuelConsumption = 0.0,
                        cost = 0
                    ),
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

@Preview
@Composable
private fun GameScreenTravelPreview() = AppTheme {
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
            Translation(
                key = "stellar_host_planet_count",
                value = "Planet Count"
            ),
            Translation(
                key = "stellar_host_type",
                value = "Host"
            ),
            Translation(
                key = "stellar_host_distance",
                value = "Distance"
            )
        )
    )
    GameScreen(
        store = Store(
            initialState = GameState(
                loading = false,
                currentContent = Content.TRAVEL,
                ship = Ship(
                    id = "1",
                    engine = Engine(
                        id = "1",
                        description = "",
                        velocity = 0.1,
                        fuelConsumption = 0.0,
                        cost = 0
                    ),
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
