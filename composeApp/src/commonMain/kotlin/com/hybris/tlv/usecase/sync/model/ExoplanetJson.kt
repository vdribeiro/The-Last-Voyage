package com.hybris.tlv.usecase.sync.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ExoplanetJson(
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_NAME) val stellarHostName: String,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_SPECTRAL_TYPE) val stellarHostSpectralType: String?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_TEMPERATURE) val stellarHostEffectiveTemperature: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_RADIUS) val stellarHostRadius: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_MASS) val stellarHostMass: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_METALLICITY) val stellarHostMetallicity: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_LUMINOSITY) val stellarHostLuminosity: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_GRAVITY) val stellarHostGravity: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_AGE) val stellarHostAge: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_DENSITY) val stellarHostDensity: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_ROTATIONAL_VELOCITY) val stellarHostRotationalVelocity: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_ROTATIONAL_PERIOD) val stellarHostRotationalPeriod: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_DISTANCE) val stellarHostDistance: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_RA) val stellarHostRa: Double?,
    @SerialName(value = StellarHostJson.Companion.STELLAR_HOST_DEC) val stellarHostDec: Double?,
    @SerialName(value = PLANET_NAME) val planetName: String,
    @SerialName(value = PLANET_STATUS) val planetStatus: String? = null,
    @SerialName(value = PLANET_ORBITAL_PERIOD) val planetOrbitalPeriod: Double?,
    @SerialName(value = PLANET_ORBIT_AXIS) val planetOrbitAxis: Double?,
    @SerialName(value = PLANET_RADIUS) val planetRadius: Double?,
    @SerialName(value = PLANET_MASS) val planetMass: Double?,
    @SerialName(value = PLANET_DENSITY) val planetDensity: Double?,
    @SerialName(value = PLANET_ECCENTRICITY) val planetEccentricity: Double?,
    @SerialName(value = PLANET_INSOLATION_FLUX) val planetInsolationFlux: Double?,
    @SerialName(value = PLANET_EQUILIBRIUM_TEMPERATURE) val planetEquilibriumTemperature: Double?,
    @SerialName(value = PLANET_OCCULTATION_DEPTH) val planetOccultationDepth: Double?,
    @SerialName(value = PLANET_INCLINATION) val planetInclination: Double?,
    @SerialName(value = PLANET_OBLIQUITY) val planetObliquity: Double?,
    @SerialName(value = PLANET_PROJECTED_OBLIQUITY) val planetProjectedObliquity: Double?
) {
    companion object {
        const val PLANET_NAME = "pl_name"
        const val PLANET_STATUS = "disposition"
        const val PLANET_ORBITAL_PERIOD = "pl_orbper"
        const val PLANET_ORBIT_AXIS = "pl_orbsmax"
        const val PLANET_RADIUS = "pl_rade"
        const val PLANET_MASS = "pl_bmasse"
        const val PLANET_DENSITY = "pl_dens"
        const val PLANET_ECCENTRICITY = "pl_orbeccen"
        const val PLANET_INSOLATION_FLUX = "pl_insol"
        const val PLANET_EQUILIBRIUM_TEMPERATURE = "pl_eqt"
        const val PLANET_OCCULTATION_DEPTH = "pl_occdep"
        const val PLANET_INCLINATION = "pl_orbincl"
        const val PLANET_OBLIQUITY = "pl_trueobliq"
        const val PLANET_PROJECTED_OBLIQUITY = "pl_projobliq"
    }
}