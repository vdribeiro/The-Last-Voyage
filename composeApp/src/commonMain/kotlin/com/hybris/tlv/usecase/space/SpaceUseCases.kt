package com.hybris.tlv.usecase.space

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlinx.coroutines.flow.Flow

internal interface SpaceUseCases {

    /**
     * Rewrites the local and remote [StellarHost] and [Planet] data.
     */
    suspend fun setup(): Flow<SyncResult>

    /**
     * Syncs the remote [StellarHost] data to local.
     */
    suspend fun syncStellarHosts(): Flow<SyncResult>

    /**
     * Syncs the remote [Planet] data to local.
     */
    suspend fun syncPlanets(): Flow<SyncResult>

    /**
     * Prepopulate local [StellarHost].
     */
    suspend fun prepopulateStellarHosts()

    /**
     * Prepopulate local [Planet].
     */
    suspend fun prepopulatePlanets()

    /**
     * Get exoplanets from the database ordered by ascending distance.
     */
    suspend fun getExoplanets(): List<StellarHost>

    /**
     * Get the nearest [n] stellar hosts of the given [stellarHost] in the [stellarHosts] list and exclude the [visited].
     */
    suspend fun getNearestStars(
        stellarHost: StellarHost,
        stellarHosts: List<StellarHost>,
        n: Int,
        visited: Set<String>
    ): List<StellarHost>
}
