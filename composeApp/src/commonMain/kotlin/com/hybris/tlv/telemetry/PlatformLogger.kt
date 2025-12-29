@file:ExcludeFromTesting

package com.hybris.tlv.telemetry

import com.hybris.tlv.test.ExcludeFromTesting

/**
 * Platform-specific logger.
 */
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
