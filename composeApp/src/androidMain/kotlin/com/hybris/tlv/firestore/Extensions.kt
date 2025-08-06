package com.hybris.tlv.firestore

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await

/**
 * Awaits the completion of the task without blocking a thread.
 * If the Job of the current coroutine is cancelled it returns null.
 */
internal suspend fun <T> Task<T>.tryAwaitWithResult(): T? = runCatching {
    await()
}.getOrNull()

/**
 * Awaits the completion of the task without blocking a thread.
 * If the Job of the current coroutine is cancelled it returns false.
 */
internal suspend fun <T> Task<T>.tryAwait(): Boolean = runCatching {
    await()
    true
}.getOrDefault(defaultValue = false)
