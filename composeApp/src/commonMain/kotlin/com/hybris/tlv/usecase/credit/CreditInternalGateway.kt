package com.hybris.tlv.usecase.credit

import com.hybris.tlv.http.Result
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credit.local.CreditLocal
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.credit.remote.CreditRemote

internal class CreditInternalGateway(
    private val creditApi: CreditRemote,
    private val creditDao: CreditLocal
): CreditInternalUseCases {

    override suspend fun syncCredits(): SyncResult =
        when (val result = creditApi.getCredits()) {
            is Result.Error -> {
                prepopulateCredits()
                SyncResult.Error(error = result.error)
            }

            is Result.Success -> {
                creditDao.rewriteCredits(credits = result.list)
                SyncResult.Success
            }
        }

    override suspend fun prepopulateCredits() {
        if (creditDao.isCreditEmpty()) {
            val credits: List<Credit> = loadFromJson(path = "files/credits.json")
            creditDao.rewriteCredits(credits = credits)
            true
        }
    }
}
