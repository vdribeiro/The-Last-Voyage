package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType

internal data class MainMenuState(
    val featureFeedback: Boolean = false,
    val featureSoon: Boolean = false,
    val featureLearn: Boolean = false,
    val featureScores: Boolean = false,
    val featureAchievements: Boolean = false,
    val featureStellarExplorer: Boolean = false,
    val featureNewGame: Boolean = false,
    val loading: Boolean = true,
    val currentContent: Content = Content.MAIN_MENU,
    val ongoingGameSession: Boolean = false,
    val learningsMap: Map<LearningType, List<Learning>> = emptyMap(),
    val developerCorner: String? = null,
    val support: String? = null,
    val formula: String? = null,
)

internal enum class Content {
    MAIN_MENU,
    LEARN_MENU,
    HOST_DEFINITION,
    PLANET_DEFINITION,
    HABITABILITY,
}

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
