package com.hybris.tlv.flow

import kotlinx.coroutines.CoroutineDispatcher

internal interface Dispatcher {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
}
