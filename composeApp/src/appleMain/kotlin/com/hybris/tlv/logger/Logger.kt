package com.hybris.tlv.logger

import platform.Foundation.NSLog

// TODO - Logger
internal actual object Logger {

    actual suspend fun setup() {
    }

    actual fun info(tag: String, message: String) {
        NSLog(format = "INFO [$tag]: $message")
    }

    actual fun error(tag: String, message: String) {
        NSLog(format = "ERROR [$tag]: $message")
    }
}
