@file:ShadowedInTesting

package com.hybris.tlv.core.locale

import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toNSDate
import platform.Foundation.NSCurrentLocaleDidChangeNotification
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSTimeZone
import platform.Foundation.preferredLanguages
import platform.Foundation.timeZoneForSecondsFromGMT
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting
import com.hybris.tlv.ui.lifecycle.observe

internal actual fun getLanguage(): String = runCatching {
    (NSLocale.preferredLanguages.first() as String).replace(oldValue = "_", newValue = "-")
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get language", throwable = it) }.getOrDefault(defaultValue = DEFAULT_LANGUAGE)

@OptIn(ExperimentalTime::class)
internal actual fun getLocalDateTime(utc: String): String = runCatching {
    val instant = Instant.parse(input = utc)
    val timeZone = TimeZone.currentSystemDefault()
    val formatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterShortStyle
        timeStyle = NSDateFormatterShortStyle
    }
    val secondsFromGmt = timeZone.offsetAt(instant = instant).totalSeconds
    formatter.timeZone = NSTimeZone.timeZoneForSecondsFromGMT(seconds = secondsFromGmt.toLong())
    return formatter.stringFromDate(date = instant.toNSDate())
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get local date time", throwable = it) }.getOrDefault(defaultValue = utc)

internal actual fun observeLocale(): Flow<String> = callbackFlow {
    runCatching {
        val observer = NSNotificationCenter.defaultCenter.observe(
            name = NSCurrentLocaleDidChangeNotification
        ) { trySend(element = getLanguage()) }

        trySend(element = getLanguage())
        awaitClose { NSNotificationCenter.defaultCenter.removeObserver(observer) }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe locale changes", throwable = it)
        trySend(element = getLanguage())
        close(cause = it)
    }
}.distinctUntilChanged()

private const val TAG = "Locale"
