package com.hybris.tlv.firestore

import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.request.QueryMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class CommonFirestore: Firestore {

    private val map = mutableMapOf<String, List<Map<String, Any>>>()

    override suspend fun disableNetwork(): Boolean = true

    override suspend fun enableNetwork(): Boolean = true

    override suspend fun getCollection(collection: String, queryMap: QueryMap): Flow<FirestoreReadResult> = flow {
        val documents = map[collection]
        if (documents == null) {
            emit(value = FirestoreReadResult.Error(error = "Error getting documents"))
            return@flow
        }
        emit(value = FirestoreReadResult.Success(documents = documents))
    }

    override suspend fun getDocument(collection: String, documentName: String): Map<String, Any>? =
        map[collection]?.find { it["id"] == documentName }

    override suspend fun setCollection(collection: String, documents: List<Map<String, Any>>): Flow<FirestoreWriteResult> = flow {
        val idsToDelete = map[collection].orEmpty().map { it["id"] }.toSet()
        val updatedList = map[collection].orEmpty().filterNot { it["id"] in idsToDelete }.toMutableList()
        val numBatches = (documents.size + BATCH_SIZE - 1) / BATCH_SIZE
        for (i in 0 until numBatches) {
            val start = i * BATCH_SIZE
            val end = minOf(a = (i + 1) * BATCH_SIZE, b = documents.size)
            val batchDocuments = documents.subList(start, end)
            val batch = mutableListOf<Map<String, Any>>()
            for (document in batchDocuments) batch.add(document)
            updatedList.addAll(elements = batch)
            emit(value = FirestoreWriteResult.PartialSuccess(documents = batchDocuments, totalDocuments = documents.size))
        }
        map[collection] = updatedList
        emit(value = FirestoreWriteResult.Success)
    }

    override suspend fun setDocument(collection: String, document: Map<String, Any>): Boolean {
        val documents = map[collection].orEmpty().toMutableList()
        documents.find { it["id"] == document["id"] }?.let { documents.remove(element = it) }
        map[collection] = documents + document
        return true
    }

    override suspend fun removeCollection(collection: String): Flow<FirestoreWriteResult> = flow {
        map.remove(key = collection)
        emit(value = FirestoreWriteResult.Success)
    }

    override suspend fun removeDocument(collection: String, documentName: String): Boolean {
        val documents = map[collection].orEmpty().toMutableList()
        documents.find { it["id"] == documentName }?.let { map[collection] = documents - it }
        return true
    }

    companion object {
        const val BATCH_SIZE = 10
    }
}
