package com.hybris.tlv.config

import kotlinx.serialization.Serializable

@Serializable
internal data class Configs(
    // Versioning
    val translationsVersion: Long = 1,
    val learningsVersion: Long = 1,
    val catastrophesVersion: Long = 1,
    val enginesVersion: Long = 1,
    val stellarHostsVersion: Long = 1,
    val planetsVersion: Long = 1,
    val eventsVersion: Long = 1,
    val achievementsVersion: Long = 1,
    val creditsVersion: Long = 1,

    // Dynamic values
    val developerCorner: String = "https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb",
    val support: String = "https://ko-fi.com/engsoneca",
    val formula: String = "https://github.com/vdribeiro/The-Last-Voyage/tree/main/composeApp/src/commonMain/kotlin/com/hybris/tlv/usecase/space/formula",

    // Feature flags
    val featureLearn: Boolean = true,
    val featureScores: Boolean = true,
    val featureAchievements: Boolean = true,
    val featureStellarExplorer: Boolean = true,
    val featureNewGame: Boolean = true,
    val featureTutorial: Boolean = true,
    val featureGame: Boolean = true,
    val featureEvents: Boolean = true,
    val featureGameOver: Boolean = true,
)
