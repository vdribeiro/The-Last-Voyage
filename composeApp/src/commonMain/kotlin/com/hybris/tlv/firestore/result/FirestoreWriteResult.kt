package com.hybris.tlv.firestore.result

internal sealed class FirestoreWriteResult {
    data class Error(val error: String): FirestoreWriteResult()
    data class PartialSuccess(val documents: List<Map<String, Any>>, val totalDocuments: Int): FirestoreWriteResult()
    data object Success: FirestoreWriteResult()
}