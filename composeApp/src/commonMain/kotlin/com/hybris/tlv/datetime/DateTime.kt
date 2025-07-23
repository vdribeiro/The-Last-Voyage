package com.hybris.tlv.datetime

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Get the current time in UTC representation in ISO8601.
 */
@OptIn(ExperimentalTime::class)
internal fun now() = Clock.System.now().toString().replace(
    regex = "\\.\\d+".toRegex(),
    replacement = ""
)
