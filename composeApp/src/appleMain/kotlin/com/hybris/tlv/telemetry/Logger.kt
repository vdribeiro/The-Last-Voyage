package com.hybris.tlv.telemetry

import platform.Foundation.NSLog

internal actual object Logger {

    actual fun info(tag: String, message: String) {
        NSLog(format = "INFO [$tag]: $message")
        SentryLogger.info(tag = tag, message = message)
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        NSLog(format = "ERROR [$tag]: $message${throwable?.let { "", throwable = it }}")
        SentryLogger.error(tag = tag, message = message, throwable = throwable)
    }
}
