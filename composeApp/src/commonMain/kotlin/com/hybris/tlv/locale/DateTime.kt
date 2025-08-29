package com.hybris.tlv.locale

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Get the current time in UTC representation in ISO8601.
 */
@OptIn(ExperimentalTime::class)
internal fun now(): String = Clock.System.now().toString().replace(
    regex = "\\.\\d+".toRegex(),
    replacement = ""
)

@OptIn(ExperimentalTime::class)
internal fun nowEpoch(): Long = Clock.System.now().epochSeconds
