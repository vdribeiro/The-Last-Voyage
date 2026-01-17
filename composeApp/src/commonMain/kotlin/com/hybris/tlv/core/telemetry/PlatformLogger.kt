package com.hybris.tlv.core.telemetry

import com.hybris.tlv.test.ExcludeFromTesting

/**
 * Platform-specific logger.
 */
@ExcludeFromTesting
internal expect object PlatformLogger {

    /**
     * Logs an informational message.
     */
    fun info(tag: String, message: String)

    /**
     * Logs an error message.
     */
    fun error(tag: String, message: String, throwable: Throwable? = null)
}
