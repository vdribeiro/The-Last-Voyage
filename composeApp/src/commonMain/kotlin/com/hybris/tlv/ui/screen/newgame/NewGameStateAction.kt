package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.ui.screen.newgame.state.ShipState
import com.hybris.tlv.ui.screen.newgame.state.ShipState.Point
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula

internal data class NewGameState(
    val currentContent: Content = Content.SHIP,
    val selectedCatastrophe: Catastrophe? = null,
    val selectedShip: ShipPrototype? = null,
    val shipState: ShipState = ShipState(
        sensorRange = Point(max = 10, min = 1, interval = 1, initialValue = 3),
        materials = Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        fuel = Point(max = 1000, min = 0, interval = 100, initialValue = 100),
        cryopods = Point(max = 1000, min = 0, interval = 100, initialValue = 100),
    ),
    val formula: Formula = Formula(),
)

internal enum class Content {
    SHIP,
    ADVANCED,
    START
}

internal sealed interface NewGameAction {
    data class SelectShip(val ship: ShipPrototype): NewGameAction
    data class SelectFormula(val formula: Formula): NewGameAction
    data object Ship: NewGameAction
    data object Advanced: NewGameAction
    data object Start: NewGameAction
    data object StartGame: NewGameAction
}

