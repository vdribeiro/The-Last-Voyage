package com.hybris.tlv.telemetry

internal actual object Logger {

    actual fun info(tag: String, message: String) {
        println("INFO [$tag]: $message")
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        println("ERROR [$tag]: $message${throwable?.let { "\n${it.printStackTrace()}" }.orEmpty()}")
    }
}
