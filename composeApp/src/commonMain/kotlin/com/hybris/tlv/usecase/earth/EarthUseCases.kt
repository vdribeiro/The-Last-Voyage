package com.hybris.tlv.usecase.earth

import com.hybris.tlv.usecase.earth.model.Catastrophe

internal interface EarthUseCases {

    /**
     * Get [Catastrophe]s from the database.
     */
    suspend fun getCatastrophes(): List<Catastrophe>
}
