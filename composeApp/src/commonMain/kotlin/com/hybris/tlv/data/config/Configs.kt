package com.hybris.tlv.data.config

import kotlinx.serialization.Serializable

/**
 * Config values.
 */
@Serializable
internal data class Configs(
    // Versioning
    val appVersion: Long = 0,
    val translationsVersion: Long = 0,
    val catastrophesVersion: Long = 0,
    val enginesVersion: Long = 0,
    val stellarHostsVersion: Long = 0,
    val planetsVersion: Long = 0,
    val eventsVersion: Long = 0,
    val achievementsVersion: Long = 0,
    val creditsVersion: Long = 0,

    // Dynamic values
    val developerCorner: String = "https://tinyurl.com/yeykkt83",
    val formula: String = "https://tinyurl.com/y53wvcm6",
)
