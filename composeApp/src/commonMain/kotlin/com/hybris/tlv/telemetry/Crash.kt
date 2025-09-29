package com.hybris.tlv.telemetry

internal expect fun setCrashHandler(onCrash: (Throwable) -> Unit)
