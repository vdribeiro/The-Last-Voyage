package com.hybris.tlv.core.telemetry

import com.hybris.tlv.domain.flag.FeatureFlags.flags
import com.hybris.tlv.infrastructure.platform.Platform
import com.hybris.tlv.infrastructure.platform.platform
import com.hybris.tlv.platform.Property
import com.hybris.tlv.test.ShadowedInTesting

/**
 * A centralized object for handling telemetry, including logging and error reporting.
 * It acts as a facade over different logging implementations.
 * In debug builds, it uses a platform-specific logger. In release builds, it uses Sentry for error reporting and feedback, provided that a Sentry DSN is available.
 */
@ShadowedInTesting
object Telemetry {

    private val useLogger get() = flags.devMode || platform == Platform.Web
    private val useSentry get() = !flags.devMode && Property.sentry.isNotBlank()

    /**
     * Initializes the telemetry services.
     * This should be called once when the application starts to set up the necessary reporting tools.
     */
    fun init() {
        if (useSentry) SentryLogger.init()
    }

    /**
     * Logs an informational [message].
     */
    fun info(tag: String, message: String) {
        if (useLogger) PlatformLogger.info(tag = tag, message = message)
        if (useSentry) SentryLogger.info(tag = tag, message = message)
    }

    /**
     * Logs an error [message], optionally with an associated [throwable].
     */
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        if (useLogger) PlatformLogger.error(tag = tag, message = message, throwable = throwable)
        if (useSentry) SentryLogger.error(tag = tag, message = message, throwable = throwable)
    }

    /**
     * Sends user-provided feedback.
     */
    fun feedback(message: String) {
        if (useSentry) SentryLogger.feedback(message = message)
    }
}
