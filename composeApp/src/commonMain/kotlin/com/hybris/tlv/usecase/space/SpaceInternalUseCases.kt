package com.hybris.tlv.usecase.space

import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.sync.model.SyncResult
import kotlinx.coroutines.flow.Flow

internal interface SpaceInternalUseCases {

    /**
     * Get exoplanet data from the NASA archive.
     */
    suspend fun getArchive(): Flow<SyncResult>

    /**
     * Syncs the remote [StellarHost] data to local.
     */
    suspend fun syncStellarHosts(): SyncResult

    /**
     * Syncs the remote [Planet] data to local.
     */
    suspend fun syncPlanets(): SyncResult

    /**
     * Prepopulate local [StellarHost].
     */
    suspend fun prepopulateStellarHosts()

    /**
     * Prepopulate local [Planet].
     */
    suspend fun prepopulatePlanets()
}
