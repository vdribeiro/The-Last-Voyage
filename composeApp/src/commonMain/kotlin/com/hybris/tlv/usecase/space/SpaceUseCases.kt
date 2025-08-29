package com.hybris.tlv.usecase.space

import com.hybris.tlv.usecase.space.model.StellarHost

internal interface SpaceUseCases {

    /**
     * Get a stellar host by [id].
     */
    suspend fun getStellarHost(id: String): StellarHost?

    /**
     * Get exoplanets.
     */
    suspend fun getExoplanets(): List<StellarHost>

    /**
     * Get the nearest [n] stellar hosts of the [stellarHost] excluding the [visited].
     */
    suspend fun getNearestStars(
        stellarHost: StellarHost,
        n: Int,
        visited: Set<String>
    ): List<StellarHost>
}
