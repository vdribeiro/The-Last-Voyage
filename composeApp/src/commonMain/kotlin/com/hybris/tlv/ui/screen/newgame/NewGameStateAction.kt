package com.hybris.tlv.ui.screen.newgame

import com.hybris.tlv.ui.theme.component.AttributePoint
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.ShipPrototype

internal sealed interface NewGameAction {
    data class SelectShip(val ship: ShipPrototype): NewGameAction
    data class SelectEngine(val engine: Engine): NewGameAction
    data object Continue: NewGameAction
}

internal sealed interface NewGameStateBuilder {
    data object Default: NewGameStateBuilder
    data class FromSavableState(
        val state: NewGameState,
        val selectedShip: ShipPrototype?,
    ): NewGameStateBuilder
}

internal data class NewGameState(
    val loading: Boolean = true,
    val currentContent: Content = Content.SHIP,
    val shipState: ShipState? = null,
    val engines: List<Engine> = emptyList(),
    val selectedCatastrophe: Catastrophe? = null,
    val selectedEngine: Engine? = null
)

internal enum class Content {
    SHIP,
    START
}

internal data class ShipState(
    val totalPoints: Int,
    val sensorRange: AttributePoint,
    val fuel: AttributePoint,
    val materials: AttributePoint,
    val cryopods: AttributePoint,
) {
    val assignedPoints: Int
        get() = sensorRange.assignedPoints +
                fuel.assignedPoints +
                materials.assignedPoints +
                cryopods.assignedPoints
    val remainingPoints: Int
        get() = totalPoints - assignedPoints
}
