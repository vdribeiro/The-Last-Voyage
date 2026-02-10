@file:OptIn(ExperimentalWasmJsInterop::class)

package com.hybris.tlv.core.locale

import kotlinx.browser.window
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import com.hybris.tlv.core.telemetry.Telemetry
import org.w3c.dom.events.Event

internal actual fun getLanguage(): String = runCatching {
    window.navigator.language
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get language", throwable = it) }.getOrDefault(defaultValue = DEFAULT_LANGUAGE)

internal actual fun getLocalDateTime(utc: String): String = runCatching {
    formatDateJs(utc = utc)
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get local date time", throwable = it) }.getOrDefault(defaultValue = utc)

private fun formatDateJs(utc: String): String = js(
    code = """
        new Date(utc).toLocaleString(undefined, {
            dateStyle: 'short',
            timeStyle: 'short'
        })
    """
)

internal actual fun observeLocale(): Flow<String> = callbackFlow {
    runCatching {
        val listener: ((Event) -> Unit) = { trySend(element = getLanguage()) }
        window.addEventListener(type = "languagechange", callback = listener)

        trySend(element = getLanguage())
        awaitClose { window.removeEventListener(type = "languagechange", callback = listener) }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe locale changes", throwable = it)
        trySend(element = getLanguage())
        close(cause = it)
    }
}.distinctUntilChanged()

private const val TAG = "Locale"
