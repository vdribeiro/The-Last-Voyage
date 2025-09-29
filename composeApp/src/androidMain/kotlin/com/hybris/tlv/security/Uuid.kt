package com.hybris.tlv.security

import com.hybris.tlv.telemetry.Logger
import java.security.SecureRandom
import java.util.UUID

internal actual fun generateUuid(): String = runCatching {
    UUID.randomUUID().toString()
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to get UUID type 4\n${it.stackTraceToString()}")
    runCatching {
        val byteArray = ByteArray(size = 16).apply { SecureRandom().nextBytes(this) }
        UUID.nameUUIDFromBytes(byteArray).toString()
    }.getOrElse { throwable ->
        Logger.error(tag = TAG, message = "Unable to get UUID type 3\n${throwable.stackTraceToString()}")
        "${System.currentTimeMillis()}-${System.nanoTime()}" // Not a real UUID, your device might be screwed...
    }
}

private const val TAG = "UUID"
