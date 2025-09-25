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

internal data class MainMenuStateBuilder(
    val currentContent: Content = Content.MAIN_MENU,
)

internal data class MainMenuState(
    val loading: Boolean,
    val featureFeedback: Boolean,
    val featureSoon: Boolean,
    val featureLearn: Boolean,
    val featureScores: Boolean,
    val featureAchievements: Boolean,
    val featureStellarExplorer: Boolean,
    val featureNewGame: Boolean,
    val featureTutorial: Boolean,
    val developerCorner: String,
    val support: String,
    val formula: String,
    val currentContent: Content,
    val ongoingGameSession: Boolean,
    val learningsMap: Map<LearningType, List<Learning>>
)

internal enum class Content {
    MAIN_MENU,
    LEARN_MENU,
    HOST_DEFINITION,
    PLANET_DEFINITION,
    HABITABILITY,
}
