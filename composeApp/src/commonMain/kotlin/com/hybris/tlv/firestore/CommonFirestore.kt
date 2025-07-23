package com.hybris.tlv.firestore

import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class CommonFirestore: Firestore {

    override suspend fun disableNetwork(): Boolean = true

    override suspend fun enableNetwork(): Boolean = true

    override suspend fun getCollection(collection: String, queryMap: QueryMap): Flow<FirestoreReadResult> =
        flowOf(value = FirestoreReadResult.Success(documents = emptyList()))

    override suspend fun getDocument(collection: String, documentName: String): Map<String, Any>? = emptyMap()

    override suspend fun setCollection(collection: String, documents: List<Map<String, Any>>): Flow<FirestoreWriteResult> =
        flowOf(value = FirestoreWriteResult.Success)

    override suspend fun setDocument(collection: String, document: Map<String, Any>): Boolean = true

    override suspend fun removeCollection(collection: String): Flow<FirestoreWriteResult> =
        flowOf(value = FirestoreWriteResult.Success)

    override suspend fun removeDocument(collection: String, documentName: String): Boolean = true
}
