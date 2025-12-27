@file:ShadowedInTesting

package com.hybris.tlv.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import com.hybris.tlv.flow.Dispatcher.Default
import com.hybris.tlv.flow.Dispatcher.IO
import com.hybris.tlv.flow.Dispatcher.Main
import com.hybris.tlv.test.ShadowedInTesting

/**
 * Dispatchers for coroutines.
 * The [Main] dispatcher is used for UI-related operations.
 * The [Default] dispatcher is used for CPU-intensive operations.
 * The [IO] dispatcher is used for I/O-related operations.
 */
internal object Dispatcher {
    val Main: CoroutineDispatcher = Dispatchers.Main
    val Default: CoroutineDispatcher = Dispatchers.Default
    val IO: CoroutineDispatcher = Dispatchers.IO
}
