package com.hybris.tlv.security

import java.security.SecureRandom
import java.util.UUID

internal actual fun generateUuid(): String = runCatching {
    UUID.randomUUID().toString()
}.getOrElse {
    val byteArray = ByteArray(size = 16).apply { SecureRandom().nextBytes(this) }
    UUID.nameUUIDFromBytes(byteArray).toString()
}
