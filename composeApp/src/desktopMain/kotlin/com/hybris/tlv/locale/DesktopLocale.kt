package com.hybris.tlv.locale

import com.hybris.tlv.usecase.translation.TranslationCache
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId

internal class DesktopLocale(): Locale {

    override fun getLanguage(): String = runCatching {
        java.util.Locale.getDefault().language.take(n = 2).lowercase()
    }.getOrDefault(defaultValue = TranslationCache.DEFAULT_LANGUAGE)

    @OptIn(ExperimentalTime::class)
    override fun getLocalDateTime(utc: String): String = runCatching {
        DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
            .format(Instant.parse(input = utc).toJavaInstant())
    }.getOrDefault(defaultValue = utc)
}
