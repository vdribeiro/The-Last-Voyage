package com.hybris.tlv.data.config

import kotlinx.serialization.Serializable

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
    val developerCorner: String = "https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb",
    val formula: String = "https://github.com/vdribeiro/The-Last-Voyage/tree/main/composeApp/src/commonMain/kotlin/com/hybris/tlv/usecase/space/formula",
)
