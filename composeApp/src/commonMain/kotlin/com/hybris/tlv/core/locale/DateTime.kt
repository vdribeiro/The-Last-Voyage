@file:OptIn(ExperimentalTime::class)

package com.hybris.tlv.core.locale

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Get the current time as an ISO-8601 formatted UTC string.
 *
 * @return A string representation of the current [Instant].
 */
internal fun now(): String = Clock.System.now().toString()

/**
 * Returns the current number of milliseconds since the Unix epoch (1970-01-01T00:00:00Z).
 *
 * @return The current timestamp in milliseconds.
 */
internal fun epoch(): Long = Clock.System.now().toEpochMilliseconds()

/**
 * Returns a string representation of a point in time far in the past.
 * This is typically used as a sentinel value for initializing timestamps.
 *
 * @return The [Instant.DISTANT_PAST] as an ISO-8601 string.
 */
internal fun distantPast(): String = Instant.DISTANT_PAST.toString()

/**
 * Determines if a specific [duration] has elapsed between [dateTime] and the current time.
 * If [dateTime] is malformed or cannot be parsed, this function defaults to using
 * the current time as the baseline, effectively returning `false`.
 *
 * @param dateTime An ISO-8601 formatted string representing the start time.
 * @param duration The [Duration] threshold to check against.
 * @return `true` if the elapsed time is strictly greater than [duration], `false` otherwise.
 */
internal fun hasTimePassed(dateTime: String, duration: Duration): Boolean {
    val parsedDateTime = runCatching { Instant.parse(input = dateTime) }.getOrDefault(defaultValue = Clock.System.now())
    val elapsedTime = Clock.System.now() - parsedDateTime
    return elapsedTime > duration
}
