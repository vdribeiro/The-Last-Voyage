package com.hybris.tlv.logger

import platform.Foundation.NSLog

internal actual object Logger {

    actual suspend fun setup() {
    }

    actual fun log(name: String, params: Map<String, Any>) {
    }

    actual fun debug(tag: String, message: String) {
        NSLog(format = "DEBUG [$tag]: $message")
    }

    actual fun info(tag: String, message: String) {
        NSLog(format = "INFO [$tag]: $message")
    }

    actual fun error(tag: String, message: String) {
        NSLog(format = "ERROR [$tag]: $message")
    }
}
