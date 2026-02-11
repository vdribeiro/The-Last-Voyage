package com.hybris.tlv.core.telemetry

/**
 * Handles telemetry, including logging and error reporting.
 */
internal interface TelemetryEngine {

    /**
     * Logs an informational [message].
     */
    fun info(tag: String, message: String)

    /**
     * Logs an error [message], optionally with an associated [throwable].
     */
    fun error(tag: String, message: String, throwable: Throwable?)

    /**
     * Sends user-provided feedback.
     */
    fun feedback(message: String)
}
