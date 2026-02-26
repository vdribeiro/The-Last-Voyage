package com.hybris.tlv.core.telemetry

internal actual object PlatformLogger {

    actual fun info(tag: String, message: String) {
        println("INFO [$tag]: $message")
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        println("ERROR [$tag]: $message${throwable?.let { "\n${it.printStackTrace()}" }.orEmpty()}")
    }
}
