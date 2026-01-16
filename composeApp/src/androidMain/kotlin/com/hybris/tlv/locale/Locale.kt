@file:ShadowedInTesting

package com.hybris.tlv.locale

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.hybris.tlv.applicationContext
import com.hybris.tlv.telemetry.Telemetry
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

internal actual fun observeLocaleChanges(onChanged: () -> Unit): Boolean = runCatching {
    val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_LOCALE_CHANGED) onChanged()
        }
    }
    val filter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
    applicationContext.registerReceiver(receiver, filter)
    true
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to observe locale changes", throwable = it) }.getOrDefault(defaultValue = false)

private const val TAG = "Locale"
