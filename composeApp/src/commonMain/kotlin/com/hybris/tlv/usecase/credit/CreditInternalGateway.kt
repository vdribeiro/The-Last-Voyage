package com.hybris.tlv.usecase.credit

import com.hybris.tlv.http.QueryMap
import com.hybris.tlv.serializer.loadFromJson
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credit.local.CreditLocal
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.credit.remote.CreditRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CreditInternalGateway(
    private val creditApi: CreditRemote,
    private val creditDao: CreditLocal
): CreditInternalUseCases {

    override suspend fun rewriteCredits(): Flow<SyncResult> {
        val credits: List<Credit> = loadFromJson(path = "files/credits.json")
        creditDao.rewriteCredits(credits = credits)
        return creditApi.rewriteCredits(credits = credits)
    }

    override suspend fun syncCredits(): Flow<SyncResult> =
        creditApi.getCredits(queryMap = QueryMap().apply {
            paginate = true
            limit = 1000
        }).map { result ->
            when (result) {
                is Result.Error -> {
                    prepopulateCredits()
                    SyncResult.Error(error = result.error)
                }

                is Result.PartialSuccess -> SyncResult.Loading(
                    progress = result.list.size.toFloat(),
                    total = result.total.toFloat()
                )

                is Result.Success -> {
                    creditDao.rewriteCredits(credits = result.list)
                    SyncResult.Success
                }
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
