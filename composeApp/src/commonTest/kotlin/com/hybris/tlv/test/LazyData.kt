package com.hybris.tlv.test

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Class designed for lazy initialization within a coroutine-based environment.
 * It ensures that [get] is only performed once and the result is cached for all subsequent calls.
 */
internal class LazyData<T>(private val get: suspend () -> T) {
    private val mutex = Mutex()
    private var _data: T? = null
    suspend fun getData(): T = _data ?: mutex.withLock { get() }.also { _data = it }
}
