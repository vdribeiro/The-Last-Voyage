package com.hybris.tlv.domain.usecase.space.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StellarHostJson(
    @SerialName(value = JsonConstants.STELLAR_HOST_NAME) val stellarHostName: String,
    @SerialName(value = JsonConstants.STELLAR_HOST_SYSTEM_NAME) val stellarHostSystemName: String?,
    @SerialName(value = JsonConstants.STELLAR_HOST_SPECTRAL_TYPE) val stellarHostSpectralType: String?,
    @SerialName(value = JsonConstants.STELLAR_HOST_TEMPERATURE) val stellarHostEffectiveTemperature: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_RADIUS) val stellarHostRadius: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_MASS) val stellarHostMass: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_METALLICITY) val stellarHostMetallicity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_LUMINOSITY) val stellarHostLuminosity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_GRAVITY) val stellarHostGravity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_AGE) val stellarHostAge: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_DENSITY) val stellarHostDensity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_ROTATIONAL_VELOCITY) val stellarHostRotationalVelocity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_ROTATIONAL_PERIOD) val stellarHostRotationalPeriod: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_DISTANCE) val stellarHostDistance: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_RA) val stellarHostRa: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_DEC) val stellarHostDec: Double?
)

@Serializable
internal data class ExoplanetJson(
    @SerialName(value = JsonConstants.STELLAR_HOST_NAME) val stellarHostName: String,
    @SerialName(value = JsonConstants.STELLAR_HOST_SPECTRAL_TYPE) val stellarHostSpectralType: String?,
    @SerialName(value = JsonConstants.STELLAR_HOST_TEMPERATURE) val stellarHostEffectiveTemperature: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_RADIUS) val stellarHostRadius: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_MASS) val stellarHostMass: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_METALLICITY) val stellarHostMetallicity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_LUMINOSITY) val stellarHostLuminosity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_GRAVITY) val stellarHostGravity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_AGE) val stellarHostAge: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_DENSITY) val stellarHostDensity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_ROTATIONAL_VELOCITY) val stellarHostRotationalVelocity: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_ROTATIONAL_PERIOD) val stellarHostRotationalPeriod: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_DISTANCE) val stellarHostDistance: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_RA) val stellarHostRa: Double?,
    @SerialName(value = JsonConstants.STELLAR_HOST_DEC) val stellarHostDec: Double?,
    @SerialName(value = JsonConstants.PLANET_NAME) val planetName: String,
    @SerialName(value = JsonConstants.PLANET_STATUS) val planetStatus: String? = null,
    @SerialName(value = JsonConstants.PLANET_ORBITAL_PERIOD) val planetOrbitalPeriod: Double?,
    @SerialName(value = JsonConstants.PLANET_ORBIT_AXIS) val planetOrbitAxis: Double?,
    @SerialName(value = JsonConstants.PLANET_RADIUS) val planetRadius: Double?,
    @SerialName(value = JsonConstants.PLANET_MASS) val planetMass: Double?,
    @SerialName(value = JsonConstants.PLANET_DENSITY) val planetDensity: Double?,
    @SerialName(value = JsonConstants.PLANET_ECCENTRICITY) val planetEccentricity: Double?,
    @SerialName(value = JsonConstants.PLANET_INSOLATION_FLUX) val planetInsolationFlux: Double?,
    @SerialName(value = JsonConstants.PLANET_EQUILIBRIUM_TEMPERATURE) val planetEquilibriumTemperature: Double?,
    @SerialName(value = JsonConstants.PLANET_OCCULTATION_DEPTH) val planetOccultationDepth: Double?,
    @SerialName(value = JsonConstants.PLANET_INCLINATION) val planetInclination: Double?,
    @SerialName(value = JsonConstants.PLANET_OBLIQUITY) val planetObliquity: Double?,
    @SerialName(value = JsonConstants.PLANET_PROJECTED_OBLIQUITY) val planetProjectedObliquity: Double?
)

internal object JsonConstants {
    const val STELLAR_HOST_NAME = "hostname"
    const val STELLAR_HOST_SYSTEM_NAME = "sy_name"
    const val STELLAR_HOST_SPECTRAL_TYPE = "st_spectype"
    const val STELLAR_HOST_TEMPERATURE = "st_teff"
    const val STELLAR_HOST_RADIUS = "st_rad"
    const val STELLAR_HOST_MASS = "st_mass"
    const val STELLAR_HOST_METALLICITY = "st_met"
    const val STELLAR_HOST_LUMINOSITY = "st_lum"
    const val STELLAR_HOST_GRAVITY = "st_logg"
    const val STELLAR_HOST_AGE = "st_age"
    const val STELLAR_HOST_DENSITY = "st_dens"
    const val STELLAR_HOST_ROTATIONAL_VELOCITY = "st_vsin"
    const val STELLAR_HOST_ROTATIONAL_PERIOD = "st_rotp"
    const val STELLAR_HOST_DISTANCE = "sy_dist"
    const val STELLAR_HOST_RA = "ra"
    const val STELLAR_HOST_DEC = "dec"
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