package com.hybris.tlv.usecase.earth

import com.hybris.tlv.usecase.earth.local.EarthLocal
import com.hybris.tlv.usecase.earth.model.Catastrophe

internal class EarthGateway(
    private val earthDao: EarthLocal
): EarthUseCases {

    override suspend fun getCatastrophes(): List<Catastrophe> =
        earthDao.getCatastrophes()
}
