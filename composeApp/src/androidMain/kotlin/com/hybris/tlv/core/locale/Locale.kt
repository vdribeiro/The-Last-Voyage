@file:ShadowedInTesting

package com.hybris.tlv.core.locale

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaZoneId
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.hybris.tlv.applicationContext
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun getLanguage(): String = runCatching {
    applicationContext.resources.configuration.locales[0].toLanguageTag()
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get language", throwable = it) }.getOrDefault(defaultValue = DEFAULT_LANGUAGE)

@OptIn(ExperimentalTime::class)
internal actual fun getLocalDateTime(utc: String): String = runCatching {
    DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(TimeZone.currentSystemDefault().toJavaZoneId())
        .format(Instant.parse(input = utc).toJavaInstant())
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get local date time", throwable = it) }.getOrDefault(defaultValue = utc)

internal actual fun observeLocale(): Flow<String> = callbackFlow {
    runCatching {
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_LOCALE_CHANGED) trySend(element = getLanguage())
            }
        }
        val filter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        applicationContext.registerReceiver(receiver, filter)

        awaitClose { applicationContext.unregisterReceiver(receiver) }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe locale changes", throwable = it)
        close(cause = it)
    }
}.distinctUntilChanged()

private const val TAG = "Locale"
