package com.hybris.tlv.usecase.exoplanet.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Score(
    val habitabilityScore: Double,
    val rocheScore: Double?,
    val habitableZoneScore: Double?,
    val planetRadiusScore: Double?,
    val planetMassScore: Double?,
    val planetTelluricityScore: Double?,
    val planetEccentricityScore: Double?,
    val planetTemperatureScore: Double?,
    val planetObliquityScore: Double?,
    val planetEsiScore: Double?,
    val stellarSpectralTypeScore: Double?,
    val stellarMassScore: Double?,
    val stellarAgeScore: Double?,
    val stellarActivityScore: Double?,
    val stellarRotationalPeriodScore: Double?,
    val stellarGravityScore: Double?,
    val stellarMetallicityScore: Double?,
    val stellarEffectiveTemperatureScore: Double?,
    val planetProtectionScore: Double?,
    val planetTidalLockingScore: Double?,
    val planetType: PlanetType?
)
