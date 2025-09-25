package com.hybris.tlv.logger

internal expect object Logger {

    fun info(tag: String, message: String)

    fun error(tag: String, message: String)
}
