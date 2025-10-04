package com.hybris.tlv.ui.screen.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.outlined.BedroomParent
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.PlanetCard
import com.hybris.tlv.ui.theme.component.Screen
import com.hybris.tlv.ui.theme.component.StatDisplay
import com.hybris.tlv.ui.theme.component.StatusBar
import com.hybris.tlv.ui.theme.component.StellarHostCard
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.space.formula.spectralTypeToDrawable
import com.hybris.tlv.usecase.space.formula.toDrawable
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

@Composable
private fun ShipContent(store: Store<GameState, GameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = storeState.ship ?: return
    val yearsTraveledTranslation = remember { getTranslation(key = "ship_years_traveled") }
    val sensorTranslation = remember { getTranslation(key = "ship_sensor") }
    val speedTranslation = remember { getTranslation(key = "ship_speed") }
    val integrityTranslation = remember { getTranslation(key = "ship_integrity") }
    val fuelTranslation = remember { getTranslation(key = "ship_fuel") }
    val materialsTranslation = remember { getTranslation(key = "ship_materials") }
    val cryopodsTranslation = remember { getTranslation(key = "ship_cryopods") }

    // Ship status with years traveled, sensor range, maximum speed, integrity, fuel, materials and cryopods
    LazyColumn(
        modifier = Modifier
            .testTag(tag = GAME_SCREEN_SHIP_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_YEARS_TRAVELED),
                icon = Icons.Outlined.Timer,
                label = yearsTraveledTranslation,
                value = ship.yearsTraveled.roundTo(decimalPlaces = 2).toString()
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_SENSOR),
                icon = Icons.Outlined.Radar,
                label = sensorTranslation,
                value = ship.sensorRange.toString()
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_SPEED),
                icon = Icons.Outlined.Speed,
                label = speedTranslation,
                value = "${ship.engine.velocity}c"
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_INTEGRITY),
                icon = Icons.Outlined.Shield,
                label = integrityTranslation,
                value = "${ship.integrity} / 100",
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_FUEL),
                icon = Icons.Outlined.LocalGasStation,
                label = fuelTranslation,
                value = ship.fuel.toString()
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_MATERIALS),
                icon = Icons.Outlined.Construction,
                label = materialsTranslation,
                value = ship.materials.toString()
            )
        }
        item {
            StatDisplay(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SHIP_CONTENT_CRYOPODS),
                icon = Icons.Outlined.BedroomParent,
                label = cryopodsTranslation,
                value = ship.cryopods.toString()
            )
        }
    }
}

@Composable
private fun SystemContent(store: Store<GameState, GameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarHost = storeState.currentStellarHost ?: return

    LazyColumn(
        modifier = Modifier
            .testTag(tag = GAME_SCREEN_SYSTEM_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        item(key = stellarHost.id) {
            StellarHostCard(
                modifier = Modifier.testTag(tag = GAME_SCREEN_SYSTEM_CONTENT_STELLAR_HOST),
                name = stellarHost.name,
                systemName = stellarHost.systemName,
                planetCount = stellarHost.planets.size,
                spectralType = stellarHost.spectralType,
                spectralTypeDrawable = stellarHost.spectralType.spectralTypeToDrawable(),
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
            )
        }
        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        items(items = stellarHost.planets, key = { it.id }) { planet ->
            PlanetCard(
                modifier = Modifier
                    .testTag(tag = GAME_SCREEN_SYSTEM_CONTENT_PLANET)
                    .clickable { store.send(action = GameAction.Settle(planet = planet)) },
                name = planet.name,
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
                habitability = planet.score?.habitabilityScore,
                type = planet.score?.planetType?.displayName,
                typeDrawable = planet.score?.planetType.toDrawable()
            )
        }
    }
}

@Composable
private fun TravelContent(store: Store<GameState, GameAction>) {
    val storeState by store.stateFlow.collectAsState()

    LazyColumn(
        modifier = Modifier
            .testTag(tag = GAME_SCREEN_TRAVEL_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        items(items = storeState.nearStellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                modifier = Modifier
                    .testTag(tag = GAME_SCREEN_TRAVEL_CONTENT_STELLAR_HOST)
                    .clickable { store.send(action = GameAction.Travel(stellarHost = stellarHost)) },
                name = stellarHost.name,
                planetCount = stellarHost.planets.size,
                spectralType = stellarHost.spectralType,
                spectralTypeDrawable = stellarHost.spectralType.spectralTypeToDrawable(),
                distance = stellarHost.distance,
            )
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
                        engine = Engine(
                            id = "1",
                            description = "",
                            velocity = 0.1
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
                        engine = Engine(
                            id = "1",
                            description = "",
                            velocity = 0.1
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
                        engine = Engine(
                            id = "1",
                            description = "",
                            velocity = 0.1
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
}
