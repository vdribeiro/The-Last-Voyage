package com.hybris.tlv.core.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual val io: CoroutineDispatcher = Dispatchers.IO
