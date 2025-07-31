package com.hybris.tlv.usecase.earth.remote

import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.earth.mapper.toCatastrophe
import com.hybris.tlv.usecase.earth.mapper.toCatastropheMap
import com.hybris.tlv.usecase.earth.model.Catastrophe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

internal class EarthApi(
    private val firestore: Firestore
): EarthRemote {

    override suspend fun rewriteCatastrophes(catastrophes: List<Catastrophe>): Flow<SyncResult> = runCatching {
        firestore.removeCollection(collection = CATASTROPHES).last()
        firestore.setCollection(collection = CATASTROPHES, documents = catastrophes.map { it.toCatastropheMap() }).map { result ->
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
        flowOf(SyncResult.Error(error = it.message.orEmpty()))
    }

    override suspend fun getCatastrophes(queryMap: QueryMap): Flow<Result<Catastrophe>> = runCatching {
        firestore.getCollection(collection = CATASTROPHES, queryMap = queryMap).map { result ->
            when (result) {
                is FirestoreReadResult.Error -> Result.Error(error = result.error)
                is FirestoreReadResult.PartialSuccess -> Result.PartialSuccess(
                    list = result.documents.map { it.toCatastrophe() },
                    total = result.totalDocuments
                )

                is FirestoreReadResult.Success -> Result.Success(list = result.documents.map { it.toCatastrophe() })
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(Result.Error(error = it.message.orEmpty()))
    }

    companion object {
        private const val TAG = "EarthApi"
        private const val CATASTROPHES = "catastrophes"
        const val CATASTROPHE_ID = "id"
        const val CATASTROPHE_NAME = "name"
        const val CATASTROPHE_DESCRIPTION = "description"
    }
}
