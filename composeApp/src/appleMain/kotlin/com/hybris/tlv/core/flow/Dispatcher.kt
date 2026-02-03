@file:ShadowedInTesting

package com.hybris.tlv.core.flow

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import com.hybris.tlv.test.ShadowedInTesting

internal actual val io: CoroutineDispatcher = Dispatchers.IO
