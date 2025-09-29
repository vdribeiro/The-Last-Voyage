@file:OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)

package com.hybris.tlv.tracker

import kotlin.experimental.ExperimentalNativeApi
import platform.Foundation.NSSetUncaughtExceptionHandler
import kotlinx.cinterop.*

private var globalOnCrash: ((Throwable) -> Unit)? = null

@CName("setCrashHandlerFromSwift")
fun setCrashHandlerFromSwift(exception: CPointer<*>) {
    // You can't easily convert the native exception to a Kotlin Throwable.
    // A string representation is more practical.
    globalOnCrash?.invoke(RuntimeException("Native iOS Crash. See device logs for details."))
}

internal actual fun setCrashHandler(onCrash: (Throwable) -> Unit) {
    globalOnCrash = onCrash
    // The actual setup must be done in Swift as shown previously,
    // which will then call back into `setCrashHandlerFromSwift`.
    NSSetUncaughtExceptionHandler { exception ->
        // This block is in native code context
        println("Caught unhandled exception: ${exception?.reason}")
        // You can try to call back into Kotlin here, but it can be unstable.
    }
}