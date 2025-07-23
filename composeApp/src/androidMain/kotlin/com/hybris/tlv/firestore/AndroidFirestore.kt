package com.hybris.tlv.firestore

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hybris.tlv.firestore.result.FirestoreReadResult
import com.hybris.tlv.firestore.result.FirestoreWriteResult
import com.hybris.tlv.http.getString
import com.hybris.tlv.http.request.QueryMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class AndroidFirestore: Firestore {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun disableNetwork(): Boolean = firestore.disableNetwork().tryAwait()

    override suspend fun enableNetwork(): Boolean = firestore.enableNetwork().tryAwait()

    private suspend fun getCollectionCount(collection: String): Int? =
        firestore.collection(collection).count().get(AggregateSource.SERVER).tryAwaitWithResult()?.count?.toInt()

    override suspend fun getCollection(collection: String, queryMap: QueryMap): Flow<FirestoreReadResult> = flow {
        val totalDocuments = getCollectionCount(collection)
        if (totalDocuments == null) {
            emit(FirestoreReadResult.Error(error = "Error getting documents"))
            return@flow
        }

        val allDocuments = mutableListOf<Map<String, Any>>()
        var lastVisibleDocument: DocumentSnapshot? = null
        var paginate = queryMap.paginate == true
        val limit = queryMap.limit ?: Long.MAX_VALUE
        var query: Query = firestore.collection(collection).apply {
            orderBy(FieldPath.documentId())
            queryMap.limit?.let { limit(it) }
        }
        do {
            if (lastVisibleDocument != null) query = query.startAfter(lastVisibleDocument)

            val documents = query.get().tryAwaitWithResult()?.documents
            if (documents == null) {
                emit(FirestoreReadResult.Error(error = "Error getting documents"))
                return@flow
            }

            val documentsInBatch = documents.mapNotNull {
                it.data?.toMutableMap()?.apply { put(key = "id", value = it.id) }
            }
            allDocuments.addAll(documentsInBatch)
            lastVisibleDocument = documents.lastOrNull()

            if (paginate) paginate = documents.size >= limit
            if (paginate) emit(FirestoreReadResult.PartialSuccess(documents = documentsInBatch, totalDocuments = totalDocuments))
            else emit(FirestoreReadResult.Success(documents = allDocuments))
        } while (paginate)
    }

    override suspend fun getDocument(collection: String, documentName: String): Map<String, Any>? =
        firestore.collection(collection).document(documentName).get().tryAwaitWithResult()?.let {
            it.data.orEmpty().toMutableMap().apply { put(key = "id", value = it.id) }
        }

    override suspend fun setCollection(collection: String, documents: List<Map<String, Any>>): Flow<FirestoreWriteResult> = flow {
        val numBatches = (documents.size + BATCH_SIZE - 1) / BATCH_SIZE
        val collectionRef = firestore.collection(collection)
        for (i in 0 until numBatches) {
            val start = i * BATCH_SIZE
            val end = minOf((i + 1) * BATCH_SIZE, documents.size)
            val batchDocuments = documents.subList(start, end)
            val batch = firestore.batch()
            for (document in batchDocuments) {
                val id = document.getString("id")
                if (id == null) {
                    emit(FirestoreWriteResult.Error(error = "Document id is null"))
                    return@flow
                }
                val documentRef = collectionRef.document(id)
                batch.set(documentRef, document.toMutableMap().apply { remove(key = "id") })
            }
            batch.commit().tryAwait()
            emit(FirestoreWriteResult.PartialSuccess(documents = batchDocuments, totalDocuments = documents.size))
        }
        emit(FirestoreWriteResult.Success)
    }

    override suspend fun setDocument(collection: String, document: Map<String, Any>): Boolean {
        return firestore.collection(collection).document(document.getString("id") ?: return false)
            .set(document.toMutableMap().apply { remove(key = "id") }).tryAwait()
    }

    override suspend fun removeCollection(collection: String): Flow<FirestoreWriteResult> = flow {
        val totalDocuments = getCollectionCount(collection)
        if (totalDocuments == null) {
            emit(FirestoreWriteResult.Error(error = "Error getting documents"))
            return@flow
        }

        do {
            val batch = firestore.batch()
            val documentsInBatch = firestore.collection(collection)
                .limit(BATCH_SIZE.toLong()).get().tryAwaitWithResult()?.documents
            if (documentsInBatch == null) {
                emit(FirestoreWriteResult.Error(error = "Error getting documents"))
                return@flow
            }
            documentsInBatch.forEach { batch.delete(it.reference) }
            batch.commit().tryAwait()
            emit(FirestoreWriteResult.PartialSuccess(documents = documentsInBatch.mapNotNull {
                it.data?.toMutableMap()?.apply { put(key = "id", value = it.id) }
            }, totalDocuments = totalDocuments))
        } while (documentsInBatch.isNotEmpty())
        emit(FirestoreWriteResult.Success)
    }

    override suspend fun removeDocument(collection: String, documentName: String): Boolean =
        firestore.collection(collection).document(documentName).delete().tryAwait()

    companion object {
        const val BATCH_SIZE = 300
    }
}
