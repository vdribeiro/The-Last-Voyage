@file:OptIn(ExperimentalTime::class)

package com.hybris.tlv.core.locale

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Get the current time in UTC representation in ISO8601.
 */
internal fun now(): String = Clock.System.now().toString()

/**
 * Returns the number of milliseconds from the epoch instant 1970-01-01T00:00:00Z.
 */
internal fun epoch(): Long = Clock.System.now().toEpochMilliseconds()

/**
 * Get the distant past time.
 */
internal fun distantPast(): String = Instant.DISTANT_PAST.toString()

/**
 * Check if [duration] has passed from [dateTime] to now.
 */
internal fun hasTimePassed(dateTime: String, duration: Duration): Boolean {
    val parsedDateTime = runCatching { Instant.parse(input = dateTime) }.getOrDefault(defaultValue = Clock.System.now())
    val elapsedTime = Clock.System.now() - parsedDateTime
    return elapsedTime > duration
}
