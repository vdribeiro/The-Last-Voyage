package com.hybris.tlv.usecase.catastrophe

import com.hybris.tlv.usecase.catastrophe.model.Catastrophe

internal interface CatastropheUseCases {

    /**
     * Get a random [Catastrophe].
     */
    suspend fun getRandomCatastrophe(): Catastrophe?
}
