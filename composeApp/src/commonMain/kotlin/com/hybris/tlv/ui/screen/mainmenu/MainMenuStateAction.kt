package com.hybris.tlv.ui.screen.mainmenu

internal sealed interface MainMenuAction {
    data object NewGame: MainMenuAction
    data object HideNavigationInfo: MainMenuAction
    data object YesNewGameDialog: MainMenuAction
    data object NoNewGameDialog: MainMenuAction
    data object HideNewGameDialog: MainMenuAction
    data object Next: MainMenuAction
    data object Scores: MainMenuAction
    data object Achievements: MainMenuAction
    data object Credits: MainMenuAction
    data object StellarExplorer: MainMenuAction
}

internal sealed interface MainMenuStateBuilder {
    data object Default: MainMenuStateBuilder
    data class FromSavableState(val state: MainMenuState): MainMenuStateBuilder
}

internal data class MainMenuState(
    val loading: Boolean = true,
    val showNavigationInfo: Boolean = false,
    val featureScores: Boolean = false,
    val featureAchievements: Boolean = false,
    val featureStellarExplorer: Boolean = false,
    val featureNewGame: Boolean = false,
    val developerCorner: String = "",
    val support: String = "",
    val ongoingGameSession: Boolean = false,
    val newGameDialog: Boolean = false,
)
