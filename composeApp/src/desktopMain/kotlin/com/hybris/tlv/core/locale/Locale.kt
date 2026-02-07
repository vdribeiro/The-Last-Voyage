@file:ShadowedInTesting

package com.hybris.tlv.core.locale

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun getLanguage(): String = runCatching {
    Locale.getDefault().toLanguageTag()
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get language", throwable = it) }.getOrDefault(defaultValue = DEFAULT_LANGUAGE)

@OptIn(ExperimentalTime::class)
internal actual fun getLocalDateTime(utc: String): String = runCatching {
    DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
        .format(Instant.parse(input = utc).toJavaInstant())
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get local date time", throwable = it) }.getOrDefault(defaultValue = utc)

internal actual fun observeLocaleChanges(): Flow<Unit> = callbackFlow {
    runCatching {
        var lastLocale = Locale.getDefault()
        val timer = Timer()
        timer.schedule(object: TimerTask() {
            override fun run() {
                val current = Locale.getDefault()
                if (current != lastLocale) {
                    lastLocale = current
                    trySend(element = Unit)
                }
            }
        }, 0, POOLING_INTERVAL_MS)

        awaitClose { timer.cancel() }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe locale changes", throwable = it)
        close(cause = it)
    }
}

private const val TAG = "Locale"
private const val POOLING_INTERVAL_MS = 3000L
