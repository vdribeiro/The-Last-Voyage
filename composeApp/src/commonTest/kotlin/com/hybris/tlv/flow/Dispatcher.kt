@file:Suppress("unused")

package com.hybris.tlv.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal object Dispatcher {
    private val Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
    val Main: CoroutineDispatcher = Unconfined
    val Default: CoroutineDispatcher = Unconfined
    val IO: CoroutineDispatcher = Unconfined
}
