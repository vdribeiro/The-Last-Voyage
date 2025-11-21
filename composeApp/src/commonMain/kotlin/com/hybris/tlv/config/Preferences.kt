package com.hybris.tlv.config

import kotlinx.serialization.Serializable
import com.hybris.tlv.locale.distantPast

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
