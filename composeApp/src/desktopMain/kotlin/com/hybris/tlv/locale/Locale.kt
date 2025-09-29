package com.hybris.tlv.locale

import com.hybris.tlv.telemetry.Logger
import com.hybris.tlv.usecase.translation.TranslationCache
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId

internal actual fun getLanguage(): String = runCatching {
    Locale.getDefault().language.take(n = 2).lowercase()
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to get language\n${it.stackTraceToString()}")
    TranslationCache.DEFAULT_LANGUAGE
}

@OptIn(ExperimentalTime::class)
internal actual fun getLocalDateTime(utc: String): String = runCatching {
    DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
        .format(Instant.parse(input = utc).toJavaInstant())
}.getOrElse {
    Logger.error(tag = TAG, message = "Unable to get local date time\n${it.stackTraceToString()}")
    utc
}

private const val TAG = "Locale"
