package com.hybris.tlv

internal actual val isDebug: Boolean get() = System.getProperty("debug") == "true"