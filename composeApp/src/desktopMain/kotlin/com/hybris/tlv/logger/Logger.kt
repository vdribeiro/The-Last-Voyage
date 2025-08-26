package com.hybris.tlv.logger

// TODO - Logger
internal actual object Logger {

    actual suspend fun setup() {
    }

    actual fun info(tag: String, message: String) {
        println("INFO [$tag]: $message")
    }

    actual fun error(tag: String, message: String) {
        println("ERROR [$tag]: $message")
    }
}
