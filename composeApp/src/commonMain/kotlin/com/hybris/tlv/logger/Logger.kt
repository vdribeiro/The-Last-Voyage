package com.hybris.tlv.logger

internal expect object Logger {

    suspend fun setup()

    fun log(name: String, params: Map<String, Any>)

    fun debug(tag: String = "DEBUG", message: String)

    fun info(tag: String = "INFO", message: String)

    fun error(tag: String = "ERROR", message: String)
}
