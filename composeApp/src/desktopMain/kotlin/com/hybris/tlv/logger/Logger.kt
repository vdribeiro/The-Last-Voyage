package com.hybris.tlv.logger

internal actual object Logger {

    actual suspend fun setup() {
    }

    actual fun log(name: String, params: Map<String, Any>) {
    }

    actual fun debug(tag: String, message: String) {
        println("DEBUG [$tag]: $message")
    }

    actual fun info(tag: String, message: String) {
        println("INFO [$tag]: $message")
    }

    actual fun error(tag: String, message: String) {
        println("ERROR [$tag]: $message")
    }
}
