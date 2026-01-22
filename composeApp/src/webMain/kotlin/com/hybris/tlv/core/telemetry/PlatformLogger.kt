package com.hybris.tlv.core.telemetry

import com.hybris.tlv.test.ExcludeFromTesting

@ExcludeFromTesting
internal actual object PlatformLogger {

    @OptIn(ExperimentalWasmJsInterop::class)
    actual fun info(tag: String, message: String) {
        val message = "INFO [$tag]: $message"
        info(message = message)
    }
    @OptIn(ExperimentalWasmJsInterop::class)
    actual fun error(tag: String, message: String, throwable: Throwable?) {
        val message = "ERROR [$tag]: $message${throwable?.let { "\n${it.stackTraceToString()}" }.orEmpty()}"
        error(message = message)
    }
}

@Suppress("UNUSED_PARAMETER")
@OptIn(ExperimentalWasmJsInterop::class)
private fun info(message: String): Unit = js(code = "console.info(message)")

@Suppress("UNUSED_PARAMETER")
@OptIn(ExperimentalWasmJsInterop::class)
private fun error(message: String): Unit = js(code = "console.error(message)")
