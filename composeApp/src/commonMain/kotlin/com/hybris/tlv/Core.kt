package com.hybris.tlv

import com.hybris.tlv.config.LocalConfig
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.UseCases
import kotlinx.coroutines.flow.Flow

internal interface Core {

    val dispatcher: Dispatcher

    val locale: Locale

    val localConfig: LocalConfig

    val useCases: UseCases

    /**
     * Warms up the core.
     */
    suspend fun setup(): Flow<SyncResult>

    /**
     * Rewrites all local and remote data.
     */
    suspend fun rewrite(): Flow<SyncResult>

    /**
     * Prepopulates all local data.
     */
    suspend fun prepopulate(): Flow<SyncResult>

    /**
     * Syncs all the remote data to local.
     */
    suspend fun sync(): Flow<SyncResult>
}
