package com.hybris.tlv.ui.screen.tutorial

internal sealed interface TutorialAction {
    data object Next: TutorialAction
}

internal sealed interface TutorialStateBuilder {
    data class Default(val newGame: Boolean): TutorialStateBuilder
    data class FromSavableState(
        val state: TutorialState,
        val newGame: Boolean
    ): TutorialStateBuilder
}

internal data class TutorialState(
    val tutorialStep: Tutorial = Tutorial.GOAL
)

internal enum class Tutorial {
    GOAL,
    SHIP,
    SYSTEM,
    TRAVEL,
    GAME_OVER
}
