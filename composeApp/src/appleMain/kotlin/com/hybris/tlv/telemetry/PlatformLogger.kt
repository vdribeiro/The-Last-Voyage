package com.hybris.tlv.telemetry

import platform.Foundation.NSLog

internal actual object PlatformLogger {

    actual fun info(tag: String, message: String) {
        NSLog(format = "INFO [$tag]: $message")
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        NSLog(format = "ERROR [$tag]: $message${throwable?.let { "\n${it.printStackTrace()}" }.orEmpty()}")
    }
}
