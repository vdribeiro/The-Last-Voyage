package com.hybris.tlv.tracker

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import platform.Foundation.NSSetUncaughtExceptionHandler

@OptIn(ExperimentalForeignApi::class)
internal actual fun setCrashHandler(onCrash: (Throwable) -> Unit) {
    NSSetUncaughtExceptionHandler(staticCFunction { exception ->
        val throwable = Throwable(exception.toString())
        onCrash(throwable)
    })
}
