package com.hybris.tlv.logger

internal object Logger {

    suspend fun setup() {}

    fun log(name: String, params: Map<String, Any>) = println("$name: $params")

    fun debug(tag: String = "DEBUG", message: String) = println("$tag: $message")

    fun info(tag: String = "INFO", message: String) = println("$tag: $message")

    fun error(tag: String = "ERROR", message: String) = println("$tag: $message")
}
