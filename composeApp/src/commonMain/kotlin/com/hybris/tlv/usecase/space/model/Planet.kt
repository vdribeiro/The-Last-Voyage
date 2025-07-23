package com.hybris.tlv.usecase.space.model

import com.hybris.tlv.usecase.exoplanet.model.Score
import kotlinx.serialization.Serializable

@Serializable
internal data class Planet(
    val id: String,
    val name: String,
    val stellarHostId: String,
    val status: PlanetStatus,
    val orbitalPeriod: Double?,
    val orbitAxis: Double?,
    val radius: Double?,
    val mass: Double?,
    val density: Double?,
    val eccentricity: Double?,
    val insolationFlux: Double?,
    val equilibriumTemperature: Double?,
    val occultationDepth: Double?,
    val inclination: Double?,
    val obliquity: Double?,
) {
    var habitability: Score? = null
}
