package com.hybris.tlv.telemetry

import android.util.Log

internal actual object Logger {

    actual fun info(tag: String, message: String) {
        Log.i(tag, message)
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}
