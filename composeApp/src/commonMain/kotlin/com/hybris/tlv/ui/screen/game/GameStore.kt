package com.hybris.tlv.ui.screen.game

import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.exoplanet.ExoplanetUseCases
import com.hybris.tlv.usecase.exoplanet.model.Params
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.model.GameSession
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlin.math.abs
import kotlin.math.ceil

internal sealed interface GameAction {
    data object Back: GameAction
    data class ChangeContent(val content: Content): GameAction
    data class Travel(val stellarHost: StellarHost): GameAction
    data class Settle(val planet: Planet): GameAction
}

internal data class GameState(
    val gameSession: GameSession? = null,
    val currentContent: Content? = null,
    val engine: Engine? = null,
    val stellarHosts: List<StellarHost> = emptyList(),
    val currentStellarHost: StellarHost? = null,
    val nearStellarHosts: List<StellarHost> = emptyList(),
    val visitedStellarHosts: Set<String> = emptySet(),
)

internal enum class Content {
    TRAVEL,
    SYSTEM,
    SHIP
}

internal class GameStore(
    dispatcher: Dispatcher,
    navigation: Navigation,
    initialState: GameState,
    private val spaceUseCases: SpaceUseCases,
    private val exoplanetUseCases: ExoplanetUseCases,
    private val gameSessionUseCases: GameSessionUseCases
): Store<GameAction, GameState>(
    dispatcher = dispatcher,
    navigation = navigation,
    initialState = initialState
) {
    init {
        setup()
    }

    private fun setup() = launchInPipeline {
        val gameSession = gameSessionUseCases.getLatestGameSession()
        if (gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(screen = Navigation.Screen.ERROR)
            return@launchInPipeline
        }

        val updatedGameSession = validateGameSession(gameSession = gameSession)
        gameSessionUseCases.updateGameSession(gameSession = updatedGameSession)

        if (updatedGameSession.integrity <= 0 || updatedGameSession.fuel <= 0) {
            navigate(screen = Navigation.Screen.GAME_OVER)
            return@launchInPipeline
        }

        val stellarHosts = spaceUseCases.getExoplanets()
        val currentStellarHost = if (updatedGameSession.currentStellarHostId == null) {
            stellarHosts.firstOrNull()
        } else stellarHosts.find { it.id == updatedGameSession.currentStellarHostId }
        if (currentStellarHost == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing stellar host")
            navigate(screen = Navigation.Screen.ERROR)
            return@launchInPipeline
        }

        var visited = updatedGameSession.visitedStellarHosts.ifEmpty {
            stellarHosts.firstOrNull()?.let { setOf(it.id) }.orEmpty()
        }
        if (visited.isEmpty()) {
            Logger.error(tag = TAG, message = "Invalid state: empty visited")
            navigate(screen = Navigation.Screen.ERROR)
            return@launchInPipeline
        }

        var nearStellarHosts = spaceUseCases.getNearestStars(
            stellarHost = currentStellarHost,
            stellarHosts = stellarHosts,
            n = updatedGameSession.sensorRange,
            visited = visited
        )

        // Nowhere to go, clear visited and recalculate
        if (nearStellarHosts.isEmpty()) {
            visited = setOf(currentStellarHost.id)
            nearStellarHosts = spaceUseCases.getNearestStars(
                stellarHost = currentStellarHost,
                stellarHosts = stellarHosts,
                n = updatedGameSession.sensorRange,
                visited = visited
            )
        }

        currentStellarHost.planets.forEach { planet ->
            planet.habitability = exoplanetUseCases.calculateHabitability(
                Params(
                    stellarHost = Params.StellarHost(
                        spectralType = currentStellarHost.spectralType,
                        effectiveTemperature = currentStellarHost.effectiveTemperature,
                        radius = currentStellarHost.radius,
                        mass = currentStellarHost.mass,
                        metallicity = currentStellarHost.metallicity,
                        luminosity = currentStellarHost.luminosity,
                        gravity = currentStellarHost.gravity,
                        age = currentStellarHost.age,
                        density = currentStellarHost.density,
                        rotationalVelocity = currentStellarHost.rotationalVelocity,
                        rotationalPeriod = currentStellarHost.rotationalPeriod
                    ),
                    planet = Params.Planet(
                        orbitalPeriod = planet.orbitalPeriod,
                        orbitAxis = planet.orbitAxis,
                        radius = planet.radius,
                        mass = planet.mass,
                        density = planet.density,
                        eccentricity = planet.eccentricity,
                        insolationFlux = planet.insolationFlux,
                        equilibriumTemperature = planet.equilibriumTemperature,
                        occultationDepth = planet.occultationDepth,
                        obliquity = planet.obliquity
                    ),
                    math = Params.Math(
                        habitableZoneWeight = gameSession.habitableZoneWeight,
                        planetRadiusWeight = gameSession.planetRadiusWeight,
                        planetMassWeight = gameSession.planetMassWeight,
                        planetTelluricityWeight = gameSession.planetTelluricityWeight,
                        planetEccentricityWeight = gameSession.planetEccentricityWeight,
                        planetTemperatureWeight = gameSession.planetTemperatureWeight,
                        planetObliquityWeight = gameSession.planetObliquityWeight,
                        planetEsiWeight = gameSession.planetEsiWeight,
                        stellarSpectralTypeWeight = gameSession.stellarSpectralTypeWeight,
                        stellarMassWeight = gameSession.stellarMassWeight,
                        stellarAgeWeight = gameSession.stellarAgeWeight,
                        stellarActivityWeight = gameSession.stellarActivityWeight,
                        stellarRotationalPeriodWeight = gameSession.stellarRotationalPeriodWeight,
                        stellarGravityWeight = gameSession.stellarGravityWeight,
                        stellarMetallicityWeight = gameSession.stellarMetallicityWeight,
                        stellarEffectiveTemperatureWeight = gameSession.stellarEffectiveTemperatureWeight,
                        planetProtectionWeight = gameSession.planetProtectionWeight,
                        planetTidalLockingWeight = gameSession.planetTidalLockingWeight,
                        planetMassLowerLimit = gameSession.planetMassLowerLimit,
                        planetMassIdealUpperLimit = gameSession.planetMassIdealUpperLimit,
                        planetMassMaxUpperLimit = gameSession.planetMassMaxUpperLimit,
                        planetRadiusLowerLimit = gameSession.planetRadiusLowerLimit,
                        planetRadiusIdealUpperLimit = gameSession.planetRadiusIdealUpperLimit,
                        planetRadiusMaxUpperLimit = gameSession.planetRadiusMaxUpperLimit,
                        stellarHostEffectiveTemperatureMaxDeviation = gameSession.stellarHostEffectiveTemperatureMaxDeviation
                    )
                )
            )
        }

        updateState {
            it.copy(
                gameSession = updatedGameSession,
                currentContent = Content.SYSTEM,
                stellarHosts = stellarHosts,
                currentStellarHost = currentStellarHost,
                nearStellarHosts = nearStellarHosts,
                visitedStellarHosts = visited
            )
        }
    }

    private fun validateGameSession(gameSession: GameSession): GameSession {
        var integrity = gameSession.integrity
        var materials = gameSession.materials
        val fuel = if (gameSession.fuel < 0) 0 else gameSession.fuel
        val cryopods = if (gameSession.cryopods < 0) 0 else gameSession.cryopods

        if (integrity <= 0) {
            // Attempt to repair the ship
            val repairAmount = abs(n = integrity) + 1
            if (materials >= repairAmount) {
                integrity = 1
                materials -= repairAmount
            } else {
                integrity = 0
                materials = 0
            }
        }

        if (materials < 0) {
            // Equalize loss
            val materialDeficit = abs(n = materials)
            integrity = if (integrity > materialDeficit) integrity - materialDeficit else 0
            materials = 0
        }

        return gameSession.copy(
            integrity = integrity,
            materials = materials,
            fuel = fuel,
            cryopods = cryopods
        )
    }

    private fun travel(state: GameState, action: GameAction.Travel) = launchInPipeline {
        val stellarHost = state.stellarHosts.find { it.id == action.stellarHost.id }
        if (state.gameSession == null || stellarHost == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session or current stellar host")
            navigate(screen = Navigation.Screen.ERROR)
            return@launchInPipeline
        }

        val visited = state.visitedStellarHosts + stellarHost.id

        val distance = ceil(x = stellarHost.distance ?: 1.0).toInt()
        val speed = 0.1  // TODO - use engine speed - using 0.1c for now
        val yearsTraveled = state.gameSession.yearsTraveled + (distance / speed)

        val fuel = state.gameSession.fuel - distance

        gameSessionUseCases.updateGameSession(
            gameSession = state.gameSession.copy(
                yearsTraveled = yearsTraveled,
                fuel = fuel,
                currentStellarHostId = stellarHost.id,
                visitedStellarHosts = visited
            )
        )

        // Hidden Cheat: If you go to the main menu in the event screen, you will circumvent the event
        navigate(screen = Navigation.Screen.EVENT)
    }

    private fun settle(state: GameState, action: GameAction.Settle) = launchInPipeline {
        if (state.gameSession == null) {
            Logger.error(tag = TAG, message = "Invalid state: missing game session")
            navigate(screen = Navigation.Screen.ERROR)
            return@launchInPipeline
        }

        gameSessionUseCases.updateGameSession(
            gameSession = state.gameSession.copy(
                settledPlanetId = action.planet.id,
                finalHabitability = action.planet.habitability?.habitabilityScore?.times(other = 100.0)
            )
        )
        navigate(screen = Navigation.Screen.GAME_OVER)
    }

    override fun reducer(state: GameState, action: GameAction) {
        when (action) {
            GameAction.Back -> navigate(screen = Navigation.Screen.MAIN_MENU)

            is GameAction.ChangeContent -> updateState {
                it.copy(currentContent = action.content)
            }

            is GameAction.Travel -> travel(state = state, action = action)

            is GameAction.Settle -> settle(state = state, action = action)
        }
    }

    companion object {
        private const val TAG = "GameStore"
    }
}
