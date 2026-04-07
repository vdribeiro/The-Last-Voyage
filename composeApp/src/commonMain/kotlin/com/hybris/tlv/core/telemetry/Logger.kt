package com.hybris.tlv.core.telemetry

import com.hybris.tlv.domain.flag.FeatureFlags.flags
import com.hybris.tlv.platform.Property

/**
 * Logger that fans out every telemetry event to up to three sinks, depending on the active feature flags and build configuration:
 *
 * - **[PlatformLogger]** – enabled in development mode. Sends to the platform's native logging facility.
 * - **[SentryLogger]** – enabled in production. Sends to Sentry.
 * - **[Console]** – Sends to an in-memory circular buffer that backs the console feature.
 */
internal class Logger: TelemetryEngine {

    private val useLogger: Boolean get() = flags.devMode
    private val useSentry: Boolean get() = !flags.devMode && Property.sentry.isNotBlank()
    private val useConsole: Boolean get() = flags.console

    init {
        if (useSentry) SentryLogger.init()
    }

    override fun info(tag: String, message: String) {
        if (useLogger) PlatformLogger.info(tag = tag, message = message)
        if (useSentry) SentryLogger.info(tag = tag, message = message)
        if (useConsole) Console.log(log = "INFO [$tag]: $message")
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        if (useLogger) PlatformLogger.error(tag = tag, message = message, throwable = throwable)
        if (useSentry) SentryLogger.error(tag = tag, message = message, throwable = throwable)
        if (useConsole) Console.log(log = "ERROR [$tag]: $message${throwable?.let { "\n${it.stackTraceToString()}" }.orEmpty()}")
    }

    override fun feedback(message: String) {
        if (useSentry) SentryLogger.feedback(message = message)
        if (useConsole) Console.log(log = "FEEDBACK: $message")
    }
}
