package com.hybris.tlv.usecase.credit.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.usecase.credit.model.Credit

internal interface CreditRemote {

    /**
     * Get credits from the API.
     */
    suspend fun getCredits(): Result<Credit>
}
