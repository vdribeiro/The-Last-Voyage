package com.hybris.tlv.logger

internal expect object Logger {

    suspend fun setup()

    fun info(tag: String = "INFO", message: String)

    fun error(tag: String = "ERROR", message: String)
}
