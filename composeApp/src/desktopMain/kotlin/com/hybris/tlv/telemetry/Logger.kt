package com.hybris.tlv.telemetry

internal actual object Logger {

    actual fun info(tag: String, message: String) {
        println("INFO [$tag]: $message")
        SentryLogger.info(tag = tag, message = message)
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        println("ERROR [$tag]: $message${throwable?.let { "", throwable = it }}")
        SentryLogger.error(tag = tag, message = message, throwable = throwable)
    }
}
