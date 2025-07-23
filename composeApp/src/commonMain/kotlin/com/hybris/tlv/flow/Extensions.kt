package com.hybris.tlv.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal fun CoroutineDispatcher.launch(block: suspend CoroutineScope.() -> Unit): Job =
    CoroutineScope(context = this).launch(context = this) { block() }
