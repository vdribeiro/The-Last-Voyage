package com.hybris.tlv.ui.screen.game

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BedroomParent
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.bottombar.GameNavigation
import com.hybris.tlv.ui.theme.component.card.PlanetCard
import com.hybris.tlv.ui.theme.component.card.StatDisplay
import com.hybris.tlv.ui.theme.component.card.StellarHostCard
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.dialog.Dialog
import com.hybris.tlv.ui.theme.component.divider.Divider
import com.hybris.tlv.ui.theme.component.list.LazyColumnWithScrollBar
import com.hybris.tlv.ui.theme.component.list.ShipContent
import com.hybris.tlv.ui.theme.component.topbar.StatusBar
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.space.formula.roundTo
import com.hybris.tlv.usecase.space.formula.spectralTypeToImage
import com.hybris.tlv.usecase.space.formula.toImage
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun GameScreen(store: Store<GameState, GameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val ship = storeState.ship

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        topBar = {
            // Status bar for sensor range, fuel, materials and cryopods
            StatusBar(
                modifier = Modifier
                    .statusBarsPadding(),
                hull = ship?.integrity?.toString(),
                fuel = ship?.fuel?.toString(),
                materials = ship?.materials?.toString(),
                cryopods = ship?.cryopods?.toString()
            )
        },
        bottomBar = {
            GameNavigation(
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
            Content.SHIP -> ShipContent(
                velocity = ship?.engine?.velocity,
                yearsTraveled = ship?.yearsTraveled,
                sensorRange = ship?.sensorRange,
                integrity = ship?.integrity,
                fuel = ship?.fuel,
                materials = ship?.materials,
                cryopods = ship?.cryopods,
            )
            Content.SYSTEM -> SystemContent(store = store)
            Content.TRAVEL -> TravelContent(store = store)
        }
    }
}

@Composable
private fun SystemContent(store: Store<GameState, GameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarHost = storeState.currentStellarHost ?: return
    var planetToSettle: Planet? by remember { mutableStateOf(value = null) }

    planetToSettle?.let {
        Dialog(
            title = getTranslation(key = "game_screen__settle", it.name),
            onConfirm = { store.send(action = GameAction.Settle(planet = it)) },
            onDismiss = { planetToSettle = null },
            onDismissRequest = { planetToSettle = null },
        )
    }

    LazyColumnWithScrollBar(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        item(key = stellarHost.id) {
            StellarHostCard(
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
            )
        }
        item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
        items(items = stellarHost.planets, key = { it.id }) { planet ->
            PlanetCard(
                modifier = Modifier
                    .clickable { planetToSettle = planet },
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
                image = planet.score?.planetType.toImage()
            )
        }
    }
}

@Composable
private fun TravelContent(store: Store<GameState, GameAction>) {
    val storeState by store.stateFlow.collectAsState()

    LazyColumnWithScrollBar(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        items(items = storeState.nearStellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                modifier = Modifier
                    .clickable { store.send(action = GameAction.Travel(stellarHost = stellarHost)) },
                name = stellarHost.name,
                planetCount = stellarHost.planets.size,
                spectralType = stellarHost.spectralType,
                spectralImage = stellarHost.spectralType.spectralTypeToImage(),
                distance = stellarHost.distance,
            )
        }
    }
}

@Preview
@Composable
private fun GameLoadingPreview() = AppTheme {
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

@Preview
@Composable
private fun GameShipPreview() = AppTheme {
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
        store = getStore(
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
private fun GameSystemPreview() = AppTheme {
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
        store = getStore(
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
private fun GameTravelPreview() = AppTheme {
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
        store = getStore(
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
