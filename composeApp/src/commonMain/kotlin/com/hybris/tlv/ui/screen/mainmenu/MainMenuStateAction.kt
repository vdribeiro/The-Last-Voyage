package com.hybris.tlv.ui.screen.mainmenu

import com.hybris.tlv.usecase.learning.model.Learning
import com.hybris.tlv.usecase.learning.model.LearningType

internal sealed interface MainMenuAction {
    data object NewGame: MainMenuAction
    data object YesNewGameDialog: MainMenuAction
    data object NoNewGameDialog: MainMenuAction
    data object HideNewGameDialog: MainMenuAction
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

internal sealed interface MainMenuStateBuilder {
    data object Default: MainMenuStateBuilder
    data class FromSavableState(val currentContent: Content): MainMenuStateBuilder
}

internal data class MainMenuState(
    val loading: Boolean = true,
    val featureLearn: Boolean = false,
    val featureScores: Boolean = false,
    val featureAchievements: Boolean = false,
    val featureStellarExplorer: Boolean = false,
    val featureNewGame: Boolean = false,
    val featureTutorial: Boolean = false,
    val developerCorner: String = "",
    val support: String = "",
    val formula: String = "",
    val currentContent: Content = Content.MAIN_MENU,
    val ongoingGameSession: Boolean = false,
    val learningsMap: Map<LearningType, List<Learning>> = emptyMap(),
    val newGameDialog: Boolean = false,
)

internal enum class Content {
    MAIN_MENU,
    LEARN_MENU,
    HOST_DEFINITION,
    PLANET_DEFINITION,
    HABITABILITY,
}
