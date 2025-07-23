package com.hybris.tlv.firestore

import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import kotlinx.coroutines.flow.Flow

/**
 * This firestore instance works on the assumption that documents contain a "id" field.
 */
internal interface Firestore {

    /**
     * Disable network access.
     * While the network is disabled, all calls will return results from cache,
     * and any write operations will be queued until network usage is re-enabled.
     */
    suspend fun disableNetwork(): Boolean

    /**
     * Enable network access.
     * Queued write operations will be sent.
     */
    suspend fun enableNetwork(): Boolean

    /**
     * Get all documents of a [collection] given a [queryMap].
     */
    suspend fun getCollection(collection: String, queryMap: QueryMap): Flow<FirestoreReadResult>

    /**
     * Get a document of a [collection] given its [documentName].
     */
    suspend fun getDocument(collection: String, documentName: String): Map<String, Any>?

    /**
     * Upsert a [collection] with [documents].
     */
    suspend fun setCollection(collection: String, documents: List<Map<String, Any>>): Flow<FirestoreWriteResult>

    /**
     * Upsert a [document] into a [collection].
     */
    suspend fun setDocument(collection: String, document: Map<String, Any>): Boolean

    /**
     * Remove a [collection].
     */
    suspend fun removeCollection(collection: String): Flow<FirestoreWriteResult>

    /**
     * Remove a document from a [collection] given its [documentName].
     */
    suspend fun removeDocument(collection: String, documentName: String): Boolean
}
