package com.hybris.tlv.tracker

import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import platform.Foundation.NSException
import platform.Foundation.NSSetUncaughtExceptionHandler

private var unhandledExceptionHook: ((Throwable) -> Unit)? = null

@OptIn(ExperimentalForeignApi::class)
private val exceptionHandler = staticCFunction<NSException?, Unit> { exception ->
    val throwable = Throwable(exception.toString())
    unhandledExceptionHook?.invoke(throwable)
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun setCrashHandler(onCrash: (Throwable) -> Unit) {
    unhandledExceptionHook = onCrash
    NSSetUncaughtExceptionHandler(arg0 = exceptionHandler)
}
