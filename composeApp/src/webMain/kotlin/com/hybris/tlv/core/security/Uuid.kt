package com.hybris.tlv.core.security

import kotlin.random.Random
import com.hybris.tlv.core.locale.epoch
import com.hybris.tlv.core.telemetry.Telemetry

@OptIn(ExperimentalWasmJsInterop::class)
internal actual fun generateUuid(): String = runCatching {
    js(code = "crypto.randomUUID()")
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get UUID type 4", throwable = it) }.getOrElse {
    runCatching {
        js(
            code = """
                ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c => (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16))
            """.trimIndent()
        )
    }.onFailure { throwable -> Telemetry.error(tag = TAG, message = "Unable to get UUID type 3", throwable = throwable) }.getOrElse {
        "${epoch()}-${Random.nextLong(from = 0, until = Long.MAX_VALUE)}" // Not a real UUID
    }
}

private const val TAG = "UUID"
