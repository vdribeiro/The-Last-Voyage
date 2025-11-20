package com.hybris.tlv.ui.screen.mainmenu

internal sealed interface MainMenuAction {
    data object NewGame: MainMenuAction
    data object YesNewGameDialog: MainMenuAction
    data object NoNewGameDialog: MainMenuAction
    data object HideNewGameDialog: MainMenuAction
    data object Next: MainMenuAction
    data object Scores: MainMenuAction
    data object Achievements: MainMenuAction
    data object Credits: MainMenuAction
    data object StellarExplorer: MainMenuAction
    data object DisableCheats: MainMenuAction
}

internal data class MainMenuState(
    val loading: Boolean = true,
    val newVersionBanner: Boolean = false,
    val cheatsEnabled: Boolean = false,
    val developerCorner: String = "",
    val support: String = "",
    val ongoingGameSession: Boolean = false,
    val newGameDialog: Boolean = false,
)
