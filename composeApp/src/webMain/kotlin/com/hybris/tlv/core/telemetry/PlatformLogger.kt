package com.hybris.tlv.core.telemetry

import com.hybris.tlv.test.ExcludeFromTesting

@ExcludeFromTesting
internal actual object PlatformLogger {

    @OptIn(ExperimentalWasmJsInterop::class)
    actual fun info(tag: String, message: String) {
        val message = "INFO [$tag]: $message"
        js(code = "console.info($message)")
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    actual fun error(tag: String, message: String, throwable: Throwable?) {
        val message = "ERROR [$tag]: $message${throwable?.let { "\n${it.stackTraceToString()}" }.orEmpty()}"
        js(code = "console.error($message)")
    }
}
