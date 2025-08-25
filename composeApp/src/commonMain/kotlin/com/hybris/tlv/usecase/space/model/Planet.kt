package com.hybris.tlv.usecase.space.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Planet(
    val id: String,
    val name: String,
    val stellarHostId: String,
    val status: PlanetStatus,            // Confirmed, Candidate, False Positive
    val orbitalPeriod: Double?,          // Earth days
    val orbitAxis: Double?,              // semi-major axis AU
    val radius: Double?,                 // Earth radii
    val mass: Double?,                   // Earth masses
    val density: Double?,                // g/cm^3
    val eccentricity: Double?,           // dimensionless (0=circular, <1=elliptical)
    val insolationFlux: Double?,         // Earth units
    val equilibriumTemperature: Double?, // Kelvin
    val occultationDepth: Double?,       // dimensionless (fraction of starlight blocked)
    val inclination: Double?,            // degrees
    val obliquity: Double?,              // degrees
) {
    var score: Score? = null
}
