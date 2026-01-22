@file:ShadowedInTesting

package com.hybris.tlv.core.locale

import kotlinx.browser.window
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ShadowedInTesting

internal actual fun getLanguage(): String = runCatching {
    window.navigator.language
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get language", throwable = it) }.getOrDefault(defaultValue = DEFAULT_LANGUAGE)

@OptIn(ExperimentalWasmJsInterop::class)
internal actual fun getLocalDateTime(utc: String): String = runCatching {
    formatDateJs(utc = utc)
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get local date time", throwable = it) }.getOrDefault(defaultValue = utc)

@Suppress("UNUSED_PARAMETER")
@OptIn(ExperimentalWasmJsInterop::class)
private fun formatDateJs(utc: String): String = js(
    code = """
        new Date(utc).toLocaleString(undefined, {
            dateStyle: 'short',
            timeStyle: 'short'
        })
    """
)

internal actual fun observeLocaleChanges(onChanged: () -> Unit): Boolean = runCatching {
    window.addEventListener(type = "languagechange") { onChanged() }
    true
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to observe locale changes", throwable = it) }.getOrDefault(defaultValue = false)

private const val TAG = "Locale"
