package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType

internal sealed interface MainMenuAction {
    data object Feedback: MainMenuAction
    data object NewGame: MainMenuAction
    data object Continue: MainMenuAction
    data object Learn: MainMenuAction
    data object Scores: MainMenuAction
    data object Achievements: MainMenuAction
    data object Credits: MainMenuAction
    data object Soon: MainMenuAction
    data object StellarExplorer: MainMenuAction
    data object HostDefinition: MainMenuAction
    data object PlanetDefinition: MainMenuAction
    data object Mechanics: MainMenuAction
    data object Habitability: MainMenuAction
}

internal data class MainMenuState(
    val featureFeedback: Boolean? = null,
    val featureSoon: Boolean? = null,
    val featureLearn: Boolean? = null,
    val featureScores: Boolean? = null,
    val featureAchievements: Boolean? = null,
    val featureStellarExplorer: Boolean? = null,
    val featureNewGame: Boolean? = null,
    val loading: Boolean? = null,
    val currentContent: Content? = null,
    val ongoingGameSession: Boolean? = null,
    val learningsMap: Map<LearningType, List<Learning>>? = null,
    val developerCorner: String? = null,
    val support: String? = null,
    val formula: String? = null
)

internal enum class Content {
    MAIN_MENU,
    LEARN_MENU,
    HOST_DEFINITION,
    PLANET_DEFINITION,
    HABITABILITY,
}
