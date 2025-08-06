package com.hybris.tlv.locale

import android.content.Context
import com.hybris.tlv.usecase.translation.TranslationCache
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId

internal class AndroidLocale(private val context: Context): Locale {

    override fun getLanguage(): String = runCatching {
        val language = context.resources.configuration.locales[0].language
        language.substring(minOf(0, language.length), minOf(2, language.length)).lowercase()
    }.getOrDefault(defaultValue = TranslationCache.DEFAULT_LANGUAGE)

    @OptIn(ExperimentalTime::class)
    override fun getLocalDateTime(utc: String): String = runCatching {
        DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
            .format(Instant.parse(input = utc).toJavaInstant())
    }.getOrDefault(defaultValue = utc)
}
