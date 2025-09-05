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
    val featureFeedback: Boolean = false,
    val featureSoon: Boolean = false,
    val featureLearn: Boolean = false,
    val featureScores: Boolean = false,
    val featureAchievements: Boolean = false,
    val featureStellarExplorer: Boolean = false,
    val featureNewGame: Boolean = false,
    val featureGame: Boolean = false,
    val featureEvents: Boolean = false,
    val featureGameOver: Boolean = false,
) {
    constructor(enableAllFeatures: Boolean): this(
        featureFeedback = enableAllFeatures,
        featureSoon = enableAllFeatures,
        featureLearn = enableAllFeatures,
        featureScores = enableAllFeatures,
        featureAchievements = enableAllFeatures,
        featureStellarExplorer = enableAllFeatures,
        featureNewGame = enableAllFeatures,
        featureGame = enableAllFeatures,
        featureEvents = enableAllFeatures,
        featureGameOver = enableAllFeatures,
    )

    fun copyValues(config: Configs) = copy(
        developerCorner = config.developerCorner,
        support = config.support,
        formula = config.formula,
    )

    fun copyFeatures(config: Configs) = copy(
        featureFeedback = config.featureFeedback,
        featureSoon = config.featureSoon,
        featureLearn = config.featureLearn,
        featureScores = config.featureScores,
        featureAchievements = config.featureAchievements,
        featureStellarExplorer = config.featureStellarExplorer,
        featureNewGame = config.featureNewGame,
        featureGame = config.featureGame,
        featureEvents = config.featureEvents,
        featureGameOver = config.featureGameOver,
    )
}
