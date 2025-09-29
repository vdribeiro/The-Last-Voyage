package com.hybris.tlv.tracker

internal expect fun setCrashHandler(onCrash: (Throwable) -> Unit)
