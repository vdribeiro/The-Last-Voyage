package com.hybris.tlv.telemetry

import com.hybris.tlv.platform.Property
import com.hybris.tlv.platform.isDebug

object Telemetry {

    private val useLogger by lazy { isDebug }
    private val useSentry by lazy { !isDebug && Property.sentry.isNotBlank() }

    fun init() {
        if (useSentry) SentryLogger.init()
    }

    fun info(tag: String, message: String) {
        if (useLogger) PlatformLogger.info(tag = tag, message = message)
        if (useSentry) SentryLogger.info(tag = tag, message = message)
    }

    fun error(tag: String, message: String, throwable: Throwable? = null) {
        if (useLogger) PlatformLogger.error(tag = tag, message = message, throwable = throwable)
        if (useSentry) SentryLogger.error(tag = tag, message = message, throwable = throwable)
    }

    fun feedback(message: String) {
        if (useSentry) SentryLogger.feedback(message = message)
    }
}
