package com.hybris.tlv.config

import kotlinx.serialization.Serializable
import com.hybris.tlv.locale.distantPast

@Serializable
internal data class Preferences(
    val cheats: Boolean = false,
    val syncTime: String = distantPast(),
    val showIntro: Boolean = true,
    val showTutorial: Boolean = true,
)
