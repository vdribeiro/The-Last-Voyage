package com.hybris.tlv.telemetry

internal object Telemetry {

    fun init() {
        SentryLogger.init()
    }

    fun info(tag: String, message: String) {
        PlatformLogger.info(tag = tag, message = message)
        SentryLogger.info(tag = tag, message = message)
    }

    fun error(tag: String, message: String, throwable: Throwable? = null) {
        PlatformLogger.error(tag = tag, message = message, throwable = throwable)
        SentryLogger.error(tag = tag, message = message, throwable = throwable)
    }

    fun feedback(message: String) {
        SentryLogger.feedback(message = message)
    }
}
