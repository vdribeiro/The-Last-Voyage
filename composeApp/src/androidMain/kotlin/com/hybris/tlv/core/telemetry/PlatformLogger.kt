package com.hybris.tlv.core.telemetry

import android.util.Log
import com.hybris.tlv.test.ExcludeFromTesting

@ExcludeFromTesting
internal actual object PlatformLogger {

    actual fun info(tag: String, message: String) {
        Log.i(tag, message)
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}
