package com.hybris.tlv.domain.usecase.space

import kotlinx.coroutines.flow.Flow
import com.hybris.tlv.domain.space.Planet
import com.hybris.tlv.domain.space.StellarHost

internal interface SpaceUseCases {

    /**
     * Sync [StellarHost]s.
     */
    suspend fun syncStellarHosts(): Boolean

    /**
     * Prepopulate [StellarHost]s.
     */
    suspend fun prepopulateStellarHosts(): Boolean

    /**
     * Sync [Planet]s.
     */
    suspend fun syncPlanets(): Boolean

    /**
     * Prepopulate [Planet]s.
     */
    suspend fun prepopulatePlanets(): Boolean

    /**
     * Get a stellar host by [id].
     */
    suspend fun getStellarHost(id: String): StellarHost?

    /**
     * Observe all exoplanets.
     */
    fun observeExoplanets(): Flow<List<StellarHost>>

    /**
     * Get the nearest [n] stellar hosts with planets of the given [stellarHost] by Euclidean distance excluding the [visited].
     */
    suspend fun getNearestStars(
        stellarHost: StellarHost,
        n: Int,
        visited: Set<String>
    ): List<StellarHost>
}
