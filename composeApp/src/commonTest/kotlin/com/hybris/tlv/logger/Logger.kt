package com.hybris.tlv.logger

internal object Logger {

    fun info(tag: String = "INFO", message: String) = println("$tag: $message")

    fun error(tag: String = "ERROR", message: String) = println("$tag: $message")
}
