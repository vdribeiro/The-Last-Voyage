@file:OptIn(ExperimentalWasmJsInterop::class)

package com.hybris.tlv.core.telemetry

actual object PlatformLogger {

    actual fun info(tag: String, message: String) {
        val message = "INFO [$tag]: $message"
        info(message = message)
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        val message = "ERROR [$tag]: $message${throwable?.let { "\n${it.stackTraceToString()}" }.orEmpty()}"
        error(message = message)
    }
}

private fun info(message: String): Unit = js(code = "console.info(message)")

private fun error(message: String): Unit = js(code = "console.error(message)")
