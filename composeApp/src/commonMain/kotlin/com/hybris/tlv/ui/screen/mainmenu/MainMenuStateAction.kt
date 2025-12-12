package com.hybris.tlv.ui.screen.mainmenu

internal sealed interface MainMenuAction {
    data object NewGame: MainMenuAction
    data object Next: MainMenuAction
    data object Scores: MainMenuAction
    data object Achievements: MainMenuAction
    data object Credits: MainMenuAction
    data object StellarExplorer: MainMenuAction
}

internal data class MainMenuState(
    val loading: Boolean = true,
    val newVersionBanner: Boolean = false,
    val developerCorner: String = "",
    val ongoingGameSession: Boolean = false,
)
