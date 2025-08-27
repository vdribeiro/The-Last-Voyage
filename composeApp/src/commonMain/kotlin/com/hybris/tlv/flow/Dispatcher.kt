package com.hybris.tlv.flow

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Dispatcher interface for coroutines.
 */
internal interface Dispatcher {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
}
