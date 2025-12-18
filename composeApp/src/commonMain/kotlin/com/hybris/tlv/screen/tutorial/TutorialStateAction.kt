package com.hybris.tlv.screen.tutorial

import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.Ship
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_CRYOPODS
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_FUEL
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_INTEGRITY
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_MATERIALS
import com.hybris.tlv.usecase.ship.model.Ship.Companion.MAX_SENSOR_RANGE

internal sealed interface TutorialAction {
    data object Next: TutorialAction
    data object Skip: TutorialAction
}

internal data class TutorialState(
    val currentContent: Content = Content.WELCOME,
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
        yearsTraveled = (0..50000).random().toDouble(),
        sensorRange = (1..MAX_SENSOR_RANGE).random(),
        integrity = (1..MAX_INTEGRITY).random(),
        fuel = (1..MAX_FUEL).random(),
        materials = (1..MAX_MATERIALS).random(),
        cryopods = (1..MAX_CRYOPODS).random(),
    )
)

internal enum class Content {
    WELCOME,
    GOAL,
    SHIP,
    TRAVEL,
    SYSTEM,
    GAME_OVER
}
