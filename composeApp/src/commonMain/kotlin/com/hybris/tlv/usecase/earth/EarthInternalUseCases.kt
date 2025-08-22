package com.hybris.tlv.usecase.earth

import com.hybris.tlv.usecase.earth.model.Catastrophe
import com.hybris.tlv.usecase.sync.model.SyncResult

internal interface EarthInternalUseCases {

    /**
     * Syncs the remote [Catastrophe] data to local.
     */
    suspend fun syncCatastrophes(): SyncResult

    /**
     * Prepopulate local [Catastrophe].
     */
    suspend fun prepopulateCatastrophes()
}
