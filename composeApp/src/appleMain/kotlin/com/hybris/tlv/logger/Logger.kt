package com.hybris.tlv.logger

import platform.Foundation.NSLog

internal actual object Logger {

    actual fun info(tag: String, message: String) {
        NSLog(format = "INFO [$tag]: $message")
    }

    actual fun error(tag: String, message: String) {
        NSLog(format = "ERROR [$tag]: $message")
    }
}
