package com.hybris.tlv.telemetry

internal expect object Logger {

    fun info(tag: String, message: String)

    fun error(tag: String, message: String)
}
