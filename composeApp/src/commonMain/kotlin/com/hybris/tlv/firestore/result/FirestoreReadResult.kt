package com.hybris.tlv.firestore.result

internal sealed interface FirestoreReadResult {
    data class Error(val error: String): FirestoreReadResult
    data class PartialSuccess(val documents: List<Map<String, Any>>, val totalDocuments: Int): FirestoreReadResult
    data class Success(val documents: List<Map<String, Any>>): FirestoreReadResult
}
