@file:Suppress("unused")

package com.hybris.tlv.telemetry

internal object Logger {

    fun info(tag: String = "INFO", message: String) = println("$tag: $message")

    fun error(tag: String = "ERROR", message: String) = println("$tag: $message")
}
