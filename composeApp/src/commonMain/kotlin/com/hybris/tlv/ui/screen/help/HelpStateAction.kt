package com.hybris.tlv.ui.screen.help

import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.model.Score
import com.hybris.tlv.usecase.space.model.StellarHost

internal sealed interface HelpAction {
    data object HostDefinition: HelpAction
    data object PlanetDefinition: HelpAction
    data object Mechanics: HelpAction
    data object Habitability: HelpAction
}

internal sealed interface HelpStateBuilder {
    data object Default: HelpStateBuilder
    data class FromSavableState(val state: HelpState): HelpStateBuilder
}

internal data class HelpState(
    val loading: Boolean = true,
    val currentContent: Content = Content.LEARN_MENU,
    val featureTutorial: Boolean = false,
    val learningsMap: Map<LearningType, List<Learning>> = emptyMap(),
    val formula: String = "",
    val stellarHost: StellarHost = StellarHost(
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
    },
    val planet: Planet = Planet(
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
)

internal enum class Content {
    LEARN_MENU,
    HOST_DEFINITION,
    PLANET_DEFINITION,
    HABITABILITY,
}
