package com.hybris.tlv.data.config

import kotlinx.serialization.Serializable
import com.hybris.tlv.core.locale.distantPast

/**
 * User preferences.
 */
@Serializable
internal data class Preferences(
    val syncTime: String = distantPast(),
    val showIntro: Boolean = true,
    val showTutorial: Boolean = true,
    val cheatIntegrity: Boolean = false,
    val cheatSensorRange: Boolean = false,
    val cheatFuel: Boolean = false,
    val cheatMaterials: Boolean = false,
    val cheatCryopods: Boolean = false
)
