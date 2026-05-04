package com.hybris.tlv.core.security

import kotlin.random.Random
import kotlin.uuid.Uuid
import com.hybris.tlv.core.locale.epoch
import com.hybris.tlv.core.telemetry.Telemetry

/**
 * Generates a universally unique identifier (UUID) using a tiered fallback strategy.
 * It attempts generation in the following order:
 * 1. **UUID v7** [uuidV7]
 * 2. **UUID v4** [uuidV4]
 * 3. **Unsecure UUID** [unsecureUuid]
 *
 * @return A 36-character string representation of the UUID.
 */
internal fun uuid(): String =
    uuidV7() ?: uuidV4() ?: unsecureUuid()

/**
 * Generates a UUID v7 string.
 * UUID v7 features a 48-bit timestamp followed by 74 bits of randomness, making them chronologically sortable.
 *
 * @return The hex-dash string representation, or `null` if the generation fails.
 */
internal fun uuidV7(): String? = runCatching {
    Uuid.generateV7().toHexDashString()
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to generate UUID v7", throwable = it)
}.getOrNull()

/**
 * Generates a UUID v4 string.
 * UUID v4 is based on 122 bits of cryptographically strong randomness.
 *
 * @return The hex-dash string representation, or `null` if the generation fails.
 */
internal fun uuidV4(): String? = runCatching {
    Uuid.generateV4().toHexDashString()
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to generate UUID v4", throwable = it)
}.getOrNull()

/**
 * Generates a non-standard "unsecure" UUID that simulates the v7 structure.
 * It is defined as "unsecure" because it uses a pseudorandom number generator which is not safe for high-entropy security requirements.
 * So this should only be used as a safety fallback when standard library generation fails. The 128-bit value is manually constructed using:
 * - **Timestamp:** 48 bits from the system clock.
 * - **Version:** 4 bits set to `7` (0x7000).
 * - **Variant:** Standard RFC 4122 variant (10xx).
 * - **Randomness:** 74 bits from [Random].
 *
 * @return A manually constructed UUID string.
 */
internal fun unsecureUuid(): String {
    // 48 bits timestamp based on system clock
    val timestamp = epoch() shl 16
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
