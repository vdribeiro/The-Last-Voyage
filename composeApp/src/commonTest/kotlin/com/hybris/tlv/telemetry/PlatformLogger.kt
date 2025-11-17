@file:Suppress("unused")

package com.hybris.tlv.telemetry

internal object PlatformLogger {

    fun info(tag: String, message: String) {
        println("$tag: $message")
    }

    fun error(tag: String, message: String) {
        println("$tag: $message")
    }
}
