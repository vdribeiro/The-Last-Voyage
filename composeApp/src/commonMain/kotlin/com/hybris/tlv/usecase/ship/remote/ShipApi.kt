package com.hybris.tlv.usecase.ship.remote

import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.ship.mapper.toEngine
import com.hybris.tlv.usecase.ship.mapper.toEngineMap
import com.hybris.tlv.usecase.ship.model.Engine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

internal class ShipApi(
    private val firestore: Firestore
): ShipRemote {

    override suspend fun rewriteEngines(engines: List<Engine>): Flow<SyncResult> = runCatching {
        firestore.removeCollection(collection = ENGINES).last()
        firestore.setCollection(collection = ENGINES, documents = engines.map { it.toEngineMap() }).map { result ->
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

    override suspend fun getEngines(queryMap: QueryMap): Flow<Result<Engine>> = runCatching {
        firestore.getCollection(collection = ENGINES, queryMap = queryMap).map { result ->
            when (result) {
                is FirestoreReadResult.Error -> Result.Error(error = result.error)
                is FirestoreReadResult.PartialSuccess -> Result.PartialSuccess(
                    list = result.documents.map { it.toEngine() },
                    total = result.totalDocuments
                )

                is FirestoreReadResult.Success -> Result.Success(list = result.documents.map { it.toEngine() })
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = Result.Error(error = it.message.orEmpty()))
    }

    companion object {
        private const val TAG = "ShipApi"
        private const val ENGINES = "engines"
        const val ENGINE_ID = "id"
        const val ENGINE_NAME = "name"
        const val ENGINE_DESCRIPTION = "description"
        const val ENGINE_VELOCITY = "velocity"
    }
}
