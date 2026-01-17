package com.hybris.tlv.core.security

import java.security.SecureRandom
import java.util.UUID
import com.hybris.tlv.telemetry.Telemetry

internal actual fun generateUuid(): String = runCatching {
    UUID.randomUUID().toString()
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get UUID type 4", throwable = it) }.getOrElse {
    runCatching {
        val byteArray = ByteArray(size = 16).apply { SecureRandom().nextBytes(this) }
        UUID.nameUUIDFromBytes(byteArray).toString()
    }.onFailure { throwable -> Telemetry.error(tag = TAG, message = "Unable to get UUID type 3", throwable = throwable) }.getOrElse {
        "${System.currentTimeMillis()}-${System.nanoTime()}" // Not a real UUID, your device might be screwed...
    }
}

private const val TAG = "UUID"
