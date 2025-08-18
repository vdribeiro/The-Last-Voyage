package com.hybris.tlv.usecase.space

import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlinx.coroutines.flow.Flow

internal interface SpaceInternalUseCases {

    /**
     * Get exoplanet data from the NASA archive.
     */
    suspend fun getArchive(): Flow<SyncResult>

    /**
     * Rewrites the local and remote [StellarHost] and [Planet] data.
     */
    suspend fun rewrite(): Flow<SyncResult>

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
}
