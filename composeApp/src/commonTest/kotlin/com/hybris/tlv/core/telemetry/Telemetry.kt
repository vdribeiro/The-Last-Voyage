@file:Suppress("unused")

package com.hybris.tlv.core.telemetry

object Telemetry {

    fun init() {}

    fun info(tag: String, message: String) {
        println("INFO: $tag - $message")
    }

    fun error(tag: String, message: String, throwable: Throwable? = null) {
        println("ERROR: $tag - $message${if (throwable == null) "" else "\n${throwable.message}"}")
    }

    fun feedback(message: String) {
        println("FEEDBACK: $message")
    }
}
