package com.hybris.tlv.domain.usecase.space.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal data class StellarHost(
    val id: String,
    val name: String,
    val systemName: String?,
    val spectralType: String?,         // Harvard spectral type with Morgan-Keenan luminosity class
    val effectiveTemperature: Double?, // Kelvin
    val radius: Double?,               // Solar radii
    val mass: Double?,                 // Solar masses
    val metallicity: Double?,          // [Fe/H] dex
    val luminosity: Double?,           // Solar luminosities
    val gravity: Double?,              // log10(cm/s2)
    val age: Double?,                  // Gigayears
    val density: Double?,              // g/cm^3
    val rotationalVelocity: Double?,   // km/s
    val rotationalPeriod: Double?,     // Earth days
    val distance: Double?,             // light-years
    val ra: Double?,                   // Right Ascension in degrees
    val dec: Double?,                  // Declination in degrees
) {
    @Transient
    val planets: MutableList<Planet> = mutableListOf()
    @Transient
    var travelOutcome: TravelOutcome? = null
    @Transient
    var score: Score? = null
}
