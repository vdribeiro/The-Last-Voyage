package com.hybris.tlv.locale

import com.hybris.tlv.usecase.translation.TranslationCache
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.preferredLanguages
import platform.Foundation.timeZoneForSecondsFromGMT

internal class IosLocale(): Locale {

    override fun getLanguage(): String = runCatching {
        (NSLocale.preferredLanguages.firstOrNull() as? String)
            ?.take(n = 2)
            ?.lowercase() ?: TranslationCache.DEFAULT_LANGUAGE
    }.getOrDefault(defaultValue = TranslationCache.DEFAULT_LANGUAGE)

    @OptIn(ExperimentalTime::class)
    override fun getLocalDateTime(utc: String): String = runCatching {
        val instant = Instant.parse(input = utc)
        val timeZone = TimeZone.currentSystemDefault()
        val formatter = NSDateFormatter().apply {
            dateStyle = NSDateFormatterShortStyle
            timeStyle = NSDateFormatterShortStyle
        }
        val secondsFromGmt = timeZone.offsetAt(instant = instant).totalSeconds
        formatter.timeZone = NSTimeZone.timeZoneForSecondsFromGMT(seconds = secondsFromGmt.toLong())
        return formatter.stringFromDate(date = instant.toNSDate())
    }.getOrDefault(defaultValue = utc)
}
