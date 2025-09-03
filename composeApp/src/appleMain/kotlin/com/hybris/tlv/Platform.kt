package com.hybris.tlv

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal actual val isDebug: Boolean get()= Platform.isDebugBinary
