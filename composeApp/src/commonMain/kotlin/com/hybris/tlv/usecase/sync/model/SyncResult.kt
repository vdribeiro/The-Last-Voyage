package com.hybris.tlv.usecase.sync.model

internal sealed interface SyncResult {
    data object Success: SyncResult
    data class Loading(val progress: Float, val total: Float): SyncResult
    data class Error(val error: String): SyncResult
}
