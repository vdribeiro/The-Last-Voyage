package com.hybris.tlv.ui.screen.tutorial

import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship

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
    val tutorialStep: Tutorial = Tutorial.GOAL,
    val ship: Ship = Ship(
        id = "",
        engine = Engine(
            id = "",
            description = "",
            velocity = 0.1,
            fuelConsumption = 0.0,
            cost = 0
        ),
        assignedPoints = 0,
        yearsTraveled = 0.0,
        sensorRange = (1..5).random(),
        integrity = (50..100).random(),
        fuel = (50..1000).random(),
        materials = (50..1000).random(),
        cryopods = (50..1000).random(),
    )
)

internal enum class Tutorial {
    GOAL,
    SHIP,
    SYSTEM,
    TRAVEL,
    GAME_OVER
}
