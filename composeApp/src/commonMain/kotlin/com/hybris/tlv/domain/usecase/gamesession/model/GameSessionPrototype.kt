package com.hybris.tlv.domain.usecase.gamesession.model

import com.hybris.tlv.domain.ship.Engine
import com.hybris.tlv.domain.usecase.ship.model.ShipPrototype
import com.hybris.tlv.domain.usecase.space.model.Formula

internal data class GameSessionPrototype(
    val ship: ShipPrototype,
    val engine: Engine,
    val formula: Formula,
)
