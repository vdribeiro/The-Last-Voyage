package com.hybris.tlv.telemetry

internal actual fun setCrashHandler(onCrash: (Throwable) -> Unit) {
    val handler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        onCrash(throwable)
        handler?.uncaughtException(thread, throwable)
    }
}
