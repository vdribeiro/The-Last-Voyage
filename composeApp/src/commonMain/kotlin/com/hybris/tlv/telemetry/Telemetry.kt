package com.hybris.tlv.telemetry

import com.hybris.tlv.platform.isDebug

internal object Telemetry {

    fun init() {
        SentryLogger.init()
    }

    fun info(tag: String, message: String) {
        if (isDebug) PlatformLogger.info(tag = tag, message = message)
        SentryLogger.info(tag = tag, message = message)
    }

    fun error(tag: String, message: String, throwable: Throwable? = null) {
        if (isDebug) PlatformLogger.error(tag = tag, message = message, throwable = throwable)
        SentryLogger.error(tag = tag, message = message, throwable = throwable)
    }

    fun feedback(message: String) {
        SentryLogger.feedback(message = message)
    }
}
