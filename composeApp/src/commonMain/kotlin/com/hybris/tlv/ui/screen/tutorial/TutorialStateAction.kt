package com.hybris.tlv.ui.screen.tutorial

internal sealed interface TutorialAction {
    data object Next: TutorialAction
}

internal data class TutorialState(
    val tutorialStep: Tutorial,
)

internal enum class Tutorial {
    GOAL,
    SHIP,
    SYSTEM,
    TRAVEL,
    GAME_OVER
}
