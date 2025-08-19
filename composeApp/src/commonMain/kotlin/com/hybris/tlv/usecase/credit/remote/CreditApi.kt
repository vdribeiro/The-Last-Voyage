package com.hybris.tlv.usecase.credit.remote

import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.credit.mapper.toCredit
import com.hybris.tlv.usecase.credit.mapper.toCreditMap
import com.hybris.tlv.usecase.credit.model.Credit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

internal class CreditApi(
    private val firestore: Firestore
): CreditRemote {

    override suspend fun rewriteCredits(credits: List<Credit>): Flow<SyncResult> = runCatching {
        firestore.removeCollection(collection = CREDITS).last()
        firestore.setCollection(collection = CREDITS, documents = credits.map { it.toCreditMap() }).map { result ->
            when (result) {
                is FirestoreWriteResult.Error -> SyncResult.Error(error = result.error)
                is FirestoreWriteResult.PartialSuccess -> SyncResult.Loading(
                    progress = result.documents.size.toFloat(),
                    total = result.totalDocuments.toFloat()
                )

                is FirestoreWriteResult.Success -> SyncResult.Success
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = SyncResult.Error(error = it.message.orEmpty()))
    }

    override suspend fun getCredits(queryMap: QueryMap): Flow<Result<Credit>> = runCatching {
        firestore.getCollection(collection = CREDITS, queryMap = queryMap).map { result ->
            when (result) {
                is FirestoreReadResult.Error -> Result.Error(error = result.error)
                is FirestoreReadResult.PartialSuccess -> Result.PartialSuccess(
                    list = result.documents.map { it.toCredit() },
                    total = result.totalDocuments
                )

                is FirestoreReadResult.Success -> Result.Success(list = result.documents.map { it.toCredit() })
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = Result.Error(error = it.message.orEmpty()))
    }

    companion object Companion {
        private const val TAG = "CreditApi"
        private const val CREDITS = "credits"
        const val CREDITS_ID = "id"
        const val CREDITS_LINK = "link"
        const val CREDITS_TYPE = "type"
    }
}
