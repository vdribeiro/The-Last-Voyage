package com.hybris.tlv.core.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.hybris.tlv.core.flow.Dispatcher.Default
import com.hybris.tlv.core.flow.Dispatcher.IO
import com.hybris.tlv.core.flow.Dispatcher.Main

/**
 * Dispatchers for coroutines.
 * The [Main] dispatcher is used for UI-related operations.
 * The [Default] dispatcher is used for CPU-intensive operations.
 * The [IO] dispatcher is used for I/O-related operations.
 */
internal object Dispatcher {
    var Main: CoroutineDispatcher = Dispatchers.Main
    var Default: CoroutineDispatcher = Dispatchers.Default
    var IO: CoroutineDispatcher = io
}

internal expect val io: CoroutineDispatcher
