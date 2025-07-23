package com.hybris.tlv.firestore

import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class IosFirestore: Firestore {

    override suspend fun disableNetwork(): Boolean = false

    override suspend fun enableNetwork(): Boolean = false

    override suspend fun getCollection(
        collection: String,
        queryMap: QueryMap
    ): Flow<FirestoreReadResult> = emptyFlow()

    override suspend fun getDocument(collection: String, documentName: String): Map<String, Any>? = emptyMap()

    override suspend fun setCollection(
        collection: String,
        documents: List<Map<String, Any>>
    ): Flow<FirestoreWriteResult> = emptyFlow()

    override suspend fun setDocument(collection: String, document: Map<String, Any>): Boolean = false

    override suspend fun removeCollection(collection: String): Flow<FirestoreWriteResult> = emptyFlow()

    override suspend fun removeDocument(collection: String, documentName: String): Boolean = false
}
