@file:OptIn(ExperimentalUuidApi::class)

package com.hybris.tlv.core.security

import kotlin.random.Random
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import com.hybris.tlv.core.telemetry.Telemetry

/**
 * Get a universally unique identifier (UUID) using the best available algorithm to prevent collisions.
 */
internal fun uuid(): String =
    uuidV7() ?: uuidV4() ?: unsecureUuid()

/**
 * Generate UUID v7: A 48-bit timestamp followed by 74 bits of randomness.
 * Returns null if it fails.
 */
internal fun uuidV7(): String? = runCatching {
    Uuid.generateV7().toHexDashString()
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to generate UUID v7", throwable = it)
}.getOrNull()

/**
 * Generate UUID v4: 122 bits of randomness.
 * Returns null if it fails.
 */
internal fun uuidV4(): String? = runCatching {
    Uuid.generateV4().toHexDashString()
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to generate UUID v4", throwable = it)
}.getOrNull()

/**
 * Generate unsecure UUID: simulates v7 with the system clock and the pseudorandom number generator.
 */
internal fun unsecureUuid(): String {
    // 48 bits timestamp based on system clock
    val timestamp = Clock.System.now().toEpochMilliseconds() shl 16
    // 4 bits to add v7 identifier
    val version7 = 0x7000L
    // 12 bits of randomness
    val randomMsb = Random.nextLong(until = 0x1000)
    // uuid identifier: sets top bit to 1, leaving 2nd bit as 0
    val variant10 = Long.MIN_VALUE
    // 62 bits of randomness
    val randomLsb = Random.nextLong() and 0x3FFFFFFFFFFFFFFFL

    return Uuid.fromLongs(
        mostSignificantBits = timestamp or version7 or randomMsb,
        leastSignificantBits = variant10 or randomLsb
    ).toHexDashString()
}

private const val TAG = "UUID"
