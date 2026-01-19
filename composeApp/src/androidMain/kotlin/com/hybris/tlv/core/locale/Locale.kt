@file:ShadowedInTesting

package com.hybris.tlv.core.locale

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId
import com.hybris.tlv.applicationContext
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun getLanguage(): String = runCatching {
    val language = applicationContext.resources.configuration.locales[0].language
    language.substring(startIndex = minOf(a = 0, b = language.length), endIndex = minOf(a = 2, b = language.length)).lowercase()
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get language", throwable = it) }.getOrDefault(defaultValue = DEFAULT_LANGUAGE)

@OptIn(ExperimentalTime::class)
internal actual fun getLocalDateTime(utc: String): String = runCatching {
    DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
        .format(Instant.parse(input = utc).toJavaInstant())
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get local date time", throwable = it) }.getOrDefault(defaultValue = utc)

private const val TAG = "Locale"
