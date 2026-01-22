package com.hybris.tlv.core.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.hybris.tlv.core.flow.Dispatcher.Default
import com.hybris.tlv.core.flow.Dispatcher.IO
import com.hybris.tlv.core.flow.Dispatcher.Main
import com.hybris.tlv.test.ShadowedInTesting

/**
 * Dispatchers for coroutines.
 * The [Main] dispatcher is used for UI-related operations.
 * The [Default] dispatcher is used for CPU-intensive operations.
 * The [IO] dispatcher is used for I/O-related operations.
 */
@ShadowedInTesting
internal object Dispatcher {
    val Main: CoroutineDispatcher = Dispatchers.Main
    val Default: CoroutineDispatcher = Dispatchers.Default
    val IO: CoroutineDispatcher = io
}

internal expect val io: CoroutineDispatcher
