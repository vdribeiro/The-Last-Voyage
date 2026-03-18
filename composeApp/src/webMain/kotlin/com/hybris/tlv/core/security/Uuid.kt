@file:OptIn(ExperimentalWasmJsInterop::class)

package com.hybris.tlv.core.security

import kotlin.random.Random
import com.hybris.tlv.core.locale.epoch
import com.hybris.tlv.core.telemetry.Telemetry

internal actual fun uuid(): String = runCatching {
    generateUuid4()
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to get UUID type 4", throwable = it)
}.getOrElse {
    runCatching {
        generateUuid3()
    }.onFailure { throwable ->
        Telemetry.error(tag = TAG, message = "Unable to get UUID type 3", throwable = throwable)
    }.getOrElse {
        "${epoch()}-${Random.nextLong(from = 0, until = Long.MAX_VALUE)}" // Not a real UUID
    }
}

private fun generateUuid4(): String = js(code = "crypto.randomUUID()")

private fun generateUuid3(): String = js(code = "([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c => (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16))")

private const val TAG = "UUID"
