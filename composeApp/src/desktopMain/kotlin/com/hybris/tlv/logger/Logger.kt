package com.hybris.tlv.logger

internal actual object Logger {

    actual fun info(tag: String, message: String) {
        println("INFO [$tag]: $message")
    }

    actual fun error(tag: String, message: String) {
        println("ERROR [$tag]: $message")
    }
}
