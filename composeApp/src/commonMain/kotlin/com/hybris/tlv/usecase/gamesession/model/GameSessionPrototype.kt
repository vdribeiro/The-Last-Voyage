package com.hybris.tlv.usecase.gamesession.model

import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Formula

internal data class GameSessionPrototype(
    val ship: ShipPrototype,
    val formula: Formula,
)
