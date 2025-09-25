package com.hybris.tlv.config

import kotlinx.serialization.Serializable

@Serializable
internal data class Configs(
    // Versioning
    val translationsVersion: Long = 0,
    val learningsVersion: Long = 0,
    val catastrophesVersion: Long = 0,
    val enginesVersion: Long = 0,
    val stellarHostsVersion: Long = 0,
    val planetsVersion: Long = 0,
    val eventsVersion: Long = 0,
    val achievementsVersion: Long = 0,
    val creditsVersion: Long = 0,

    // Dynamic values
    val developerCorner: String = "https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb",
    val support: String = "https://ko-fi.com/engsoneca",
    val formula: String = "https://github.com/vdribeiro/The-Last-Voyage/tree/main/composeApp/src/commonMain/kotlin/com/hybris/tlv/usecase/space/formula",

    // Feature flags
    val featureFeedback: Boolean = true,
    val featureSoon: Boolean = true,
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
