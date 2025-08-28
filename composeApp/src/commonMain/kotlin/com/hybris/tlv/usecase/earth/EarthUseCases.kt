package com.hybris.tlv.usecase.earth

import com.hybris.tlv.usecase.earth.model.Catastrophe

internal interface EarthUseCases {

    /**
     * Get a random [Catastrophe].
     */
    suspend fun getRandomCatastrophe(): Catastrophe?
}
