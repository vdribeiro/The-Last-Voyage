package com.hybris.tlv.locale

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId
import com.hybris.tlv.telemetry.Telemetry

internal actual fun getLanguage(): String = runCatching {
    Locale.getDefault().language.take(n = 2).lowercase()
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to get language", throwable = it)
    DEFAULT_LANGUAGE
}

@OptIn(ExperimentalTime::class)
internal actual fun getLocalDateTime(utc: String): String = runCatching {
    DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
        .format(Instant.parse(input = utc).toJavaInstant())
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to get local date time", throwable = it)
    utc
}

private const val TAG = "Locale"
