package com.hybris.tlv.usecase.space.remote.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StellarHostJson(
    @SerialName(value = STELLAR_HOST_SYSTEM_NAME) val stellarHostSystemName: String,
    @SerialName(value = STELLAR_HOST_NAME) val stellarHostName: String,
    @SerialName(value = STELLAR_HOST_SPECTRAL_TYPE) val stellarHostSpectralType: String?,
    @SerialName(value = STELLAR_HOST_TEMPERATURE) val stellarHostEffectiveTemperature: Double?,
    @SerialName(value = STELLAR_HOST_RADIUS) val stellarHostRadius: Double?,
    @SerialName(value = STELLAR_HOST_MASS) val stellarHostMass: Double?,
    @SerialName(value = STELLAR_HOST_METALLICITY) val stellarHostMetallicity: Double?,
    @SerialName(value = STELLAR_HOST_LUMINOSITY) val stellarHostLuminosity: Double?,
    @SerialName(value = STELLAR_HOST_GRAVITY) val stellarHostGravity: Double?,
    @SerialName(value = STELLAR_HOST_AGE) val stellarHostAge: Double?,
    @SerialName(value = STELLAR_HOST_DENSITY) val stellarHostDensity: Double?,
    @SerialName(value = STELLAR_HOST_ROTATIONAL_VELOCITY) val stellarHostRotationalVelocity: Double?,
    @SerialName(value = STELLAR_HOST_ROTATIONAL_PERIOD) val stellarHostRotationalPeriod: Double?,
    @SerialName(value = STELLAR_HOST_DISTANCE) val stellarHostDistance: Double?,
    @SerialName(value = STELLAR_HOST_RA) val stellarHostRa: Double?,
    @SerialName(value = STELLAR_HOST_DEC) val stellarHostDec: Double?
) {
    companion object {
        const val STELLAR_HOST_SYSTEM_NAME = "sy_name"
        const val STELLAR_HOST_NAME = "hostname"
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
    }
}