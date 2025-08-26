package com.hybris.tlv.logger

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

internal actual object Logger {

    actual suspend fun setup() {
        val id = FirebaseInstallations.getInstance().id.tryAwaitWithResult() ?: return
        Firebase.crashlytics.setUserId(id)
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

    /**
     * Awaits the completion of the task without blocking a thread.
     * If the Job of the current coroutine is cancelled it returns null.
     */
    private suspend fun <T> Task<T>.tryAwaitWithResult(): T? = runCatching { await() }.getOrNull()
}
