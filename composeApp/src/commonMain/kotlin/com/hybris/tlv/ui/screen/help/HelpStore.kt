package com.hybris.tlv.ui.screen.help

import kotlinx.coroutines.Job
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.learning.LearningUseCases
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.model.Score
import com.hybris.tlv.usecase.space.model.StellarHost

internal class HelpStore(
    dispatcher: Dispatcher,
    navigation: NavigationManager,
    audioPlayer: AudioPlayer,
    private val config: ConfigManager,
    private val learningUseCases: LearningUseCases
): Store<HelpState, HelpAction>(
    dispatcher = dispatcher,
    navigation = navigation,
    audioPlayer = audioPlayer,
    initialState = HelpState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
        Telemetry.info(tag = TAG, message = "Setup")
        val learningsMap = learningUseCases.getLearnings().groupBy { it.type }
        val stellarHost = StellarHost(
            id = "Valar",
            name = "Valar",
            systemName = "Arda",
            spectralType = "G3V",
            effectiveTemperature = 5678.0,
            radius = 1.0,
            mass = 7.0,
            metallicity = 3.0,
            luminosity = 9.0,
            gravity = 2.0,
            age = 1.2,
            density = 2.1,
            rotationalVelocity = 10.0,
            rotationalPeriod = 50.0,
            distance = 9000.0,
            ra = 901.2,
            dec = 345.6,
        ).apply {
            planets.add(
                element = Planet(
                    id = "ME",
                    name = "ME",
                    stellarHostId = "Valar",
                    status = PlanetStatus.FALSE,
                    orbitalPeriod = null,
                    orbitAxis = null,
                    radius = null,
                    mass = null,
                    density = null,
                    eccentricity = null,
                    insolationFlux = null,
                    equilibriumTemperature = null,
                    occultationDepth = null,
                    inclination = null,
                    obliquity = null,
                )
            )
        }
        val planet = Planet(
            id = "Edoras",
            name = "Edoras",
            stellarHostId = "Valar",
            status = PlanetStatus.CANDIDATE,
            orbitalPeriod = 123.0,
            orbitAxis = 1.2,
            radius = 5.1,
            mass = 2.3,
            density = 3.2,
            eccentricity = 0.5,
            insolationFlux = 2.1,
            equilibriumTemperature = 666.9,
            occultationDepth = 0.01,
            inclination = 1.8,
            obliquity = 50.0,
        ).apply {
            score = Score(
                habitabilityScore = 1.0,
                confidenceScore = 1.0,
                planetType = PlanetType.SUPERHABITABLE_PLANET,
                rocheScore = null,
                habitableZoneKopparapuScore = null,
                habitableZoneKastingScore = null,
                planetRadiusScore = null,
                planetMassScore = null,
                planetTelluricityScore = null,
                planetEccentricityScore = null,
                planetTemperatureScore = null,
                planetObliquityScore = null,
                planetEsiScore = null,
                stellarSpectralTypeScore = null,
                stellarMassScore = null,
                stellarAgeScore = null,
                stellarActivityScore = null,
                stellarRotationalPeriodScore = null,
                stellarGravityScore = null,
                stellarMetallicityScore = null,
                stellarEffectiveTemperatureScore = null,
                planetProtectionScore = null,
                planetTidalLockingScore = null
            )
        }

        updateState {
            it.copy(
                loading = false,
                featureTutorial = config.localConfigs.featureTutorial,
                formula = config.localConfigs.formula,
                learningsMap = learningsMap,
                stellarHost = stellarHost,
                planet = planet
            )
        }
        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    override fun goBack(state: HelpState) {
        when (state.currentContent) {
            Content.LEARN_MENU -> super.goBack(state = state)
            Content.HOST_DEFINITION,
            Content.PLANET_DEFINITION,
            Content.HABITABILITY -> updateState { it.copy(currentContent = Content.LEARN_MENU) }
        }
    }

    override fun reducer(state: HelpState, action: HelpAction) {
        when (action) {
            HelpAction.HostDefinition -> updateState { it.copy(currentContent = Content.HOST_DEFINITION) }
            HelpAction.PlanetDefinition -> updateState { it.copy(currentContent = Content.PLANET_DEFINITION) }
            HelpAction.Mechanics -> navigate(screen = Screen.Tutorial)
            HelpAction.Habitability -> updateState { it.copy(currentContent = Content.HABITABILITY) }
        }
    }

    companion object Companion {
        private const val TAG = "Help"
    }
}
