package com.hybris.tlv.usecase.event.remote

import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import com.hybris.tlv.usecase.event.mapper.toEvent
import com.hybris.tlv.usecase.event.mapper.toEventMap
import com.hybris.tlv.usecase.event.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

internal class EventApi(
    private val firestore: Firestore
): EventRemote {

    override suspend fun rewriteEvents(events: List<Event>): Flow<SyncResult> = runCatching {
        firestore.removeCollection(collection = EVENTS).last()
        firestore.setCollection(collection = EVENTS, documents = events.map { it.toEventMap() }).map { result ->
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

    override suspend fun getEvents(queryMap: QueryMap): Flow<Result<Event>> = runCatching {
        firestore.getCollection(collection = EVENTS, queryMap = queryMap).map { result ->
            when (result) {
                is FirestoreReadResult.Error -> Result.Error(error = result.error)
                is FirestoreReadResult.PartialSuccess -> Result.PartialSuccess(
                    list = result.documents.map { it.toEvent() },
                    total = result.totalDocuments
                )

                is FirestoreReadResult.Success -> Result.Success(list = result.documents.map { it.toEvent() })
            }
        }
    }.getOrElse {
        Logger.error(tag = TAG, message = it.message.orEmpty())
        flowOf(value = Result.Error(error = it.message.orEmpty()))
    }

    companion object {
        private const val TAG = "EventApi"
        private const val EVENTS = "events"
        const val EVENT_ID = "id"
        const val EVENT_NAME = "name"
        const val EVENT_DESCRIPTION = "description"
        const val EVENT_PARENT_ID = "parentId"
        const val EVENT_OUTCOME = "outcome"
    }
}
