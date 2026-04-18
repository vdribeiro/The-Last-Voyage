package com.hybris.tlv.core.telemetry

import com.hybris.tlv.core.telemetry.Telemetry.engine

/**
 * Handles telemetry, including logging, error reporting and user feedback.
 */
object Telemetry {

    var engine: TelemetryEngine? = null

    /**
     * Send an informational message to the telemetry [engine].
     */
    fun info(tag: String, message: String) {
        engine?.info(tag = tag, message = message)
    }

    /**
     * Send an error message to the telemetry [engine] optionally with an associated [throwable].
     */
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        engine?.error(tag = tag, message = message, throwable = throwable)
    }

    /**
     * Send a user-provided feedback message to the telemetry [engine].
     */
    fun feedback(message: String) {
        engine?.feedback(message = message)
    }
}
