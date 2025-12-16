package com.hybris.tlv.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * Dispatchers for coroutines.
 * The kotlin dispatchers are not directly used so that this custom implementation can be overridden in tests.
 * The [Main] dispatcher is used for UI-related operations.
 * The [Default] dispatcher is used for CPU-intensive operations.
 * The [IO] dispatcher is used for I/O-related operations.
 */
internal object Dispatcher {
    val Main: CoroutineDispatcher = Dispatchers.Main
    val Default: CoroutineDispatcher = Dispatchers.Default
    val IO: CoroutineDispatcher = Dispatchers.IO
}
