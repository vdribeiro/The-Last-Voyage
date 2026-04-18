package com.hybris.tlv.core.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual val io: CoroutineDispatcher = Dispatchers.IO
