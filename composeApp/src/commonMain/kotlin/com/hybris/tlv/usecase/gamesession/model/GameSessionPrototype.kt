package com.hybris.tlv.usecase.gamesession.model

import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.space.model.Math

internal data class GameSessionPrototype(
    val ship: ShipPrototype,
    val math: Math,
)
