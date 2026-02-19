package com.hybris.tlv.core.security

import java.security.SecureRandom
import java.util.UUID
import kotlin.random.Random
import com.hybris.tlv.core.locale.epoch
import com.hybris.tlv.core.telemetry.Telemetry

internal actual fun uuid(): String = runCatching {
    UUID.randomUUID().toString()
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get UUID type 4", throwable = it) }.getOrElse {
    runCatching {
        val byteArray = ByteArray(size = 16).apply { SecureRandom().nextBytes(this) }
        UUID.nameUUIDFromBytes(byteArray).toString()
    }.onFailure { throwable -> Telemetry.error(tag = TAG, message = "Unable to get UUID type 3", throwable = throwable) }.getOrElse {
        "${epoch()}-${Random.nextLong(from = 0, until = Long.MAX_VALUE)}" // Not a real UUID
    }
}

private const val TAG = "UUID"
