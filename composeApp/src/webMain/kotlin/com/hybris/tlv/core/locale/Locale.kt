@file:ShadowedInTesting
@file:OptIn(ExperimentalWasmJsInterop::class)

package com.hybris.tlv.core.locale

import kotlinx.browser.window
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun getLanguage(): String = runCatching {
    window.navigator.language
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get language", throwable = it) }.getOrDefault(defaultValue = DEFAULT_LANGUAGE)

internal actual fun getLocalDateTime(utc: String): String = runCatching {
    formatDateJs(utc = utc)
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get local date time", throwable = it) }.getOrDefault(defaultValue = utc)

@Suppress("UNUSED_PARAMETER")
private fun formatDateJs(utc: String): String = js(
    code = """
        new Date(utc).toLocaleString(undefined, {
            dateStyle: 'short',
            timeStyle: 'short'
        })
    """
)

internal actual fun observeLocaleChanges(): Flow<Unit> = callbackFlow {
    runCatching {
        window.addEventListener(type = "languagechange") { trySend(element = Unit) }

        awaitClose { window.removeEventListener(type = "languagechange") {} }
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to observe locale changes", throwable = it)
        close(cause = it)
    }
}

private const val TAG = "Locale"
