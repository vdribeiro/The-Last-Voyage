package com.hybris.tlv.telemetry

/**
 * Platform-specific logger.
 */
internal expect object Logger {

    /**
     * Logs an informational message.
     */
    fun info(tag: String, message: String)

    /**
     * Logs an error message.
     */
    fun error(tag: String, message: String, throwable: Throwable? = null)
}
