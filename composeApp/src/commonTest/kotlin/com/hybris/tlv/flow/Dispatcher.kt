@file:Suppress("unused")

package com.hybris.tlv.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal object Dispatcher {
    val Main: CoroutineDispatcher = Dispatchers.Unconfined
    val Default: CoroutineDispatcher = Dispatchers.Unconfined
    val IO: CoroutineDispatcher = Dispatchers.Unconfined
}
