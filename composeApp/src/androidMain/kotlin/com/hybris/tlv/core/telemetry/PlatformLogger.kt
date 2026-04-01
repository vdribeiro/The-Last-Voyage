package com.hybris.tlv.core.telemetry

import android.util.Log

internal actual object PlatformLogger {

    actual fun info(tag: String, message: String) {
        log(message = message) { Log.i(tag, it) }
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        log(message = "$message${throwable?.let { "\n${it.stackTraceToString()}" }.orEmpty()}") { Log.e(tag, it) }
    }

    /**
     * Log in chunks to work around the 4KB buffer limit of the Logcat.
     */
    private fun log(message: String, log: (String) -> Unit) {
        if (message.length <= CHUNK_SIZE) log(message)
        else message.chunked(CHUNK_SIZE).forEachIndexed { index, chunk -> log("($index) $chunk") }
    }

    /**
     * Chunk size for log messages.
     * It assumes an average of 2-bytes per character.
     */
    private const val CHUNK_SIZE = 2000
}
