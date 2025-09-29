package com.hybris.tlv.telemetry

import android.util.Log

internal actual object Logger {

    actual fun info(tag: String, message: String) {
        Log.i(tag, message)
        SentryLogger.info(tag = tag, message = message)
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
        SentryLogger.error(tag = tag, message = message, throwable = throwable)
    }
}
