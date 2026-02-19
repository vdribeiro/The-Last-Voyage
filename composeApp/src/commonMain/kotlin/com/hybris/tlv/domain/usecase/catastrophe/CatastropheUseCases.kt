package com.hybris.tlv.domain.usecase.catastrophe

import com.hybris.tlv.domain.usecase.catastrophe.model.Catastrophe

internal interface CatastropheUseCases {

    /**
     * Sync [Catastrophe]s.
     */
    suspend fun syncCatastrophes(): Boolean

    /**
     * Prepopulate [Catastrophe]s.
     */
    suspend fun prepopulateCatastrophes(): Boolean

    /**
     * Get a random [Catastrophe].
     */
    suspend fun getRandomCatastrophe(): Catastrophe?

    /**
     * Get all [Catastrophe]s.
     */
    suspend fun getCatastrophes(): List<Catastrophe>
}
