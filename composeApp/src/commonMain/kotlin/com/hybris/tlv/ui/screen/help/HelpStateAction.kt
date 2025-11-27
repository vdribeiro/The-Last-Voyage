package com.hybris.tlv.ui.screen.help

import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost

internal sealed interface HelpAction {
    data object Navigation: HelpAction
    data object HostDefinition: HelpAction
    data object HostType: HelpAction
    data object PlanetDefinition: HelpAction
    data object PlanetType: HelpAction
    data object Mechanics: HelpAction
    data object Habitability: HelpAction
    data class VersionClick(val reset: Boolean): HelpAction
}

internal data class HelpState(
    val loading: Boolean = true,
    val currentContent: Content = Content.LEARN_MENU,
    val learningsMap: Map<LearningType, List<Learning>> = emptyMap(),
    val formula: String? = null,
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
    val showSnackbar: Boolean = false
)

internal enum class Content {
    LEARN_MENU,
    NAVIGATION,
    HOST_DEFINITION,
    HOST_TYPE,
    PLANET_DEFINITION,
    PLANET_TYPE,
    HABITABILITY,
}
