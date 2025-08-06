package com.hybris.tlv.logger

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.hybris.tlv.firestore.tryAwaitWithResult

internal actual object Logger {

    actual suspend fun setup() {
        val id = FirebaseInstallations.getInstance().id.tryAwaitWithResult() ?: return
        Firebase.crashlytics.setUserId(id)
        Firebase.analytics.setUserId(id)
    }

    actual fun log(name: String, params: Map<String, Any>) {
        val bundle = Bundle().apply {
            params.forEach { (key, value) ->
                when (value) {
                    is Boolean -> putBoolean(key, value)
                    is Number -> putDouble(key, value.toDouble())
                    else -> putString(key, value.toString())
                }
            }
        }
        Firebase.analytics.logEvent(name, bundle)
    }

    actual fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun info(tag: String, message: String) {
        Log.i(tag, message)
        Firebase.crashlytics.log("$tag: $message")
    }

    actual fun error(tag: String, message: String) {
        Log.e(tag, message)
        Firebase.crashlytics.log("$tag: $message")
        Firebase.crashlytics.recordException(Exception(message))
    }
}
