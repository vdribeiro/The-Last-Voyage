@file:Suppress("unused")

package com.hybris.tlv.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
internal object Dispatcher {
    private val Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
    var Main: CoroutineDispatcher = Unconfined
    var Default: CoroutineDispatcher = Unconfined
    var IO: CoroutineDispatcher = Unconfined

    fun setTestDispatcher(dispatcher: CoroutineDispatcher) {
        Main = dispatcher
        Default = dispatcher
        IO = dispatcher
        Dispatchers.setMain(dispatcher = dispatcher)
    }

    fun reset() {
        Main = Unconfined
        Default = Unconfined
        IO = Unconfined
        Dispatchers.resetMain()
    }
}
