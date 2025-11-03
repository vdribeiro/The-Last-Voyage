package com.hybris.tlv.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal object Dispatchers {
    val Main: CoroutineDispatcher = Dispatchers.Unconfined
    val Default: CoroutineDispatcher = Dispatchers.Unconfined
    val IO: CoroutineDispatcher = Dispatchers.Unconfined
}
