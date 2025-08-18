package com.hybris.tlv.usecase.ship

import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.ship.model.Engine

internal class ShipGateway(
    private val shipDao: ShipLocal
): ShipUseCases {

    override suspend fun getEngines(): List<Engine> =
        shipDao.getEngines().sortedByDescending { it.velocity }
}
