package com.hybris.tlv.usecase.space.model

import kotlinx.serialization.Serializable

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
    val gravity: Double?,              // log(g) in cgs units
    val age: Double?,                  // Gigayears
    val density: Double?,              // g/cm^3
    val rotationalVelocity: Double?,   // km/s
    val rotationalPeriod: Double?,     // Earth days
    val distance: Double?,             // light-years
    val ra: Double?,                   // Right Ascension in degrees
    val dec: Double?,                  // Declination in degrees
) {
    val planets: MutableList<Planet> = mutableListOf()
    var travelOutcome: TravelOutcome? = null
}
