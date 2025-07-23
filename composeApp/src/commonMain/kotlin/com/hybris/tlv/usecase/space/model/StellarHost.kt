package com.hybris.tlv.usecase.space.model

import kotlinx.serialization.Serializable

@Serializable
internal data class StellarHost(
    val id: String,
    val systemName: String,
    val name: String,
    val spectralType: String?,
    val effectiveTemperature: Double?,
    val radius: Double?,
    val mass: Double?,
    val metallicity: Double?,
    val luminosity: Double?,
    val gravity: Double?,
    val age: Double?,
    val density: Double?,
    val rotationalVelocity: Double?,
    val rotationalPeriod: Double?,
    val distance: Double?,
    val ra: Double?,
    val dec: Double?,
) {
    val planets: MutableList<Planet> = mutableListOf()
    var travelOutcome: TravelOutcome? = null
}
