package com.hybris.tlv.usecase.earth.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.usecase.earth.model.Catastrophe

internal interface EarthRemote {

    /**
     * Get catastrophes from the API.
     */
    suspend fun getCatastrophes(): Result<Catastrophe>
}
