package com.hybris.tlv.core.security

import kotlin.random.Random
import platform.Foundation.NSUUID
import com.hybris.tlv.core.locale.epoch
import com.hybris.tlv.core.telemetry.Telemetry

internal actual fun uuid(): String = runCatching {
    NSUUID().UUIDString()
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get UUID", throwable = it) }.getOrElse {
    "${epoch()}-${Random.nextLong(from = 0, until = Long.MAX_VALUE)}" // Not a real UUID
}

private const val TAG = "UUID"