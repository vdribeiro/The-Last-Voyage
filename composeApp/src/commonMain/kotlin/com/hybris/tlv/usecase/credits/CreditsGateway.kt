package com.hybris.tlv.usecase.credits

import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credits.local.CreditsLocal
import com.hybris.tlv.usecase.credits.model.Credits
import com.hybris.tlv.usecase.credits.remote.CreditsRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import thelastvoyage.composeapp.generated.resources.Res

internal class CreditsGateway(
    private val creditsApi: CreditsRemote,
    private val creditsDao: CreditsLocal
): CreditsUseCases {

    private suspend fun loadCreditsFromJson(): List<Credits> = runCatching {
        val jsonString = Res.readBytes(path = "files/credits.json").decodeToString()
        json.decodeFromString<List<Credits>>(string = jsonString)
    }.getOrDefault(defaultValue = emptyList())

    override suspend fun setup(): Flow<SyncResult> {
        val credits = loadCreditsFromJson()
        creditsDao.rewriteCredits(credits = credits)
        return creditsApi.rewriteCredits(credits = credits)
    }

    override suspend fun syncCredits(): Flow<SyncResult> =
        creditsApi.getCredits(queryMap = QueryMap().apply {
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
                    creditsDao.rewriteCredits(credits = result.list)
                    SyncResult.Success
                }
            }
        }

    override suspend fun prepopulateCredits() {
        if (creditsDao.isCreditsEmpty()) {
            val credits = loadCreditsFromJson()
            creditsDao.rewriteCredits(credits = credits)
            true
        }
    }

    override suspend fun getCredits(): List<Credits> =
        creditsDao.getCredits()
}
