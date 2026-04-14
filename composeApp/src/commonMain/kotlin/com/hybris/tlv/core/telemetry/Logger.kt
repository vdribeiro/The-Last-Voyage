package com.hybris.tlv.core.telemetry

import com.hybris.tlv.domain.flag.FeatureFlags.flags
import com.hybris.tlv.platform.Property

/**
 * A composite [TelemetryEngine] that implements a fan-out pattern to multiple sinks.
 * This class orchestrates the distribution of telemetry data to platform-specific loggers, remote crash reporting services, and internal memory buffers.
 * It uses a combination of compile-time properties and runtime feature flags to gate data flow.
 *
 * ### Supported Sinks:
 * - **[PlatformLogger]**: Targeted for local development. Routes to native system logs.
 * - **[SentryLogger]**: Targeted for production. Routes to Sentry when a valid DSN is provided.
 * - **[Console]**: An in-memory buffer accessible for on-device debugging.
 */
internal class Logger: TelemetryEngine {

    /**
     * Determines if logs should be sent to the platform's native output.
     * Enabled strictly during development.
     */
    private val useLogger: Boolean get() = flags.devMode

    /**
     * Determines if events should be synced to Sentry.
     * Requires production mode and a non-blank Sentry property.
     */
    private val useSentry: Boolean get() = !flags.devMode && Property.sentry.isNotBlank()

    /**
     * Determines if logs should be stored in the [Console] buffer.
     */
    private val useConsole: Boolean get() = flags.console

    init {
        if (useSentry) SentryLogger.init()
    }

    /**
     * Distributes informational logs to all active sinks.
     */
    override fun info(tag: String, message: String) {
        if (useLogger) PlatformLogger.info(tag = tag, message = message)
        if (useSentry) SentryLogger.info(tag = tag, message = message)
        if (useConsole) Console.log(log = "INFO [$tag]: $message")
    }

    /**
     * Distributes error reports and stack traces to all active sinks.
     */
    override fun error(tag: String, message: String, throwable: Throwable?) {
        if (useLogger) PlatformLogger.error(tag = tag, message = message, throwable = throwable)
        if (useSentry) SentryLogger.error(tag = tag, message = message, throwable = throwable)
        if (useConsole) Console.log(log = "ERROR [$tag]: $message${throwable?.let { "\n${it.stackTraceToString()}" }.orEmpty()}")
    }

    /**
     * Distributes user feedback.
     * Feedback is typically bypassed by [PlatformLogger] and [Console] to avoid exposing user-sensitive text.
     */
    override fun feedback(message: String) {
        if (useSentry) SentryLogger.feedback(message = message)
    }
}