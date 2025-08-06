package com.hybris.tlv.usecase

import kotlinx.coroutines.flow.Flow

internal sealed interface Result<T> {
    data class Success<T>(val list: List<T>): Result<T>
    data class PartialSuccess<T>(val list: List<T>, val total: Int): Result<T>
    data class Error<T>(val error: String): Result<T>
}

internal sealed interface SyncResult {
    data object Success: SyncResult
    data class Loading(val progress: Float, val total: Float): SyncResult
    data class Error(val error: String): SyncResult
}

internal fun Array<SyncResult>.combine(): SyncResult {
    val errorList = filterIsInstance<SyncResult.Error>()
    if (errorList.isNotEmpty()) return SyncResult.Error(error = errorList.joinToString(separator = "\n") { it.error })
    if (all { it is SyncResult.Success }) return SyncResult.Success
    val progress = map { if (it is SyncResult.Loading) it.progress / it.total else 1f }.sum() / size
    return SyncResult.Loading(progress = progress, total = 1f)
}

internal suspend fun Flow<SyncResult>.collectProgress(update: (Float) -> Unit) = collect { result ->
    val progress = when (result) {
        is SyncResult.Error, SyncResult.Success -> 1f
        is SyncResult.Loading -> if (result.total > 0f) result.progress / result.total else 1f
    }
    update(progress)
}
