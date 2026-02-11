package com.hybris.tlv.core.telemetry

object Telemetry {

    internal var engine: TelemetryEngine? = null

    fun info(tag: String, message: String) {
        engine?.info(tag = tag, message = message)
    }

    fun error(tag: String, message: String, throwable: Throwable? = null) {
        engine?.error(tag = tag, message = message, throwable = throwable)
    }

    fun feedback(message: String) {
        engine?.feedback(message = message)
    }
}
