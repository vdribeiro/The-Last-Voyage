package com.hybris.tlv.locale

import com.hybris.tlv.applicationContext
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.translation.TranslationCache
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId

internal actual fun getLanguage(): String = runCatching {
    val language = applicationContext.resources.configuration.locales[0].language
    language.substring(startIndex = minOf(a = 0, b = language.length), endIndex = minOf(a = 2, b = language.length)).lowercase()
}.getOrElse {
    Telemetry.error(tag = TAG, message = "Unable to get language", throwable = it)
    TranslationCache.DEFAULT_LANGUAGE
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
