package com.hybris.tlv.usecase.space.remote

import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.space.remote.result.ExoplanetsResult
import kotlinx.coroutines.flow.Flow

internal interface SpaceRemote {

    /**
     * Get stellar hosts from the NASA Exoplanet Archive given the [queryMap].
     */
    suspend fun getStellarHostsArchive(queryMap: QueryMap = QueryMap()): ExoplanetsResult

    /**
     * Get exoplanets from the NASA Exoplanet Archive given the [queryMap].
     */
    suspend fun getExoplanetsArchive(queryMap: QueryMap = QueryMap()): ExoplanetsResult

    /**
     * Get K2 exoplanets from the NASA Exoplanet Archive given the [queryMap].
     */
    suspend fun getK2ExoplanetsArchive(queryMap: QueryMap = QueryMap()): ExoplanetsResult

    /**
     * Rewrite [stellarHosts] in the API.
     */
    suspend fun rewriteStellarHosts(stellarHosts: List<StellarHost>): Flow<SyncResult>

    /**
     * Rewrite [planets] in the API.
     */
    suspend fun rewritePlanets(planets: List<Planet>): Flow<SyncResult>

    /**
     * Get stellar systems from the API given the [queryMap].
     */
    suspend fun getStellarHosts(queryMap: QueryMap = QueryMap()): Flow<Result<StellarHost>>

    /**
     * Get planets from the API given the [queryMap].
     */
    suspend fun getPlanets(queryMap: QueryMap = QueryMap()): Flow<Result<Planet>>
}
