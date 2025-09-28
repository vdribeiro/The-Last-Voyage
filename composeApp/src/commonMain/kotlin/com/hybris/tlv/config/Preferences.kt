package com.hybris.tlv.config

import com.hybris.tlv.locale.distantPast
import kotlinx.serialization.Serializable

@Serializable
internal data class Preferences(
    val syncTime: String = distantPast(),
    val showTutorial: Boolean = true,
)
