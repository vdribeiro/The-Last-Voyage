@file:ShadowedInTesting

package com.hybris.tlv.ui.lifecycle

import kotlinx.browser.document
import kotlinx.browser.window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.hybris.tlv.test.ShadowedInTesting
import org.w3c.dom.events.Event

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
internal actual fun Register(
    key: Any,
    onBackground: () -> Unit,
    onForeground: () -> Unit,
) {
    DisposableEffect(key) {
        val visibilityListener: (Event) -> Unit = {
            if (isHidden()) {
                onBackground()
            } else {
                onForeground()
            }
        }

        val focusListener: (Event) -> Unit = { onForeground() }
        val blurListener: (Event) -> Unit = { onBackground() }

        document.addEventListener(type = "visibilitychange", callback = visibilityListener)
        window.addEventListener(type = "focus", callback = focusListener)
        window.addEventListener(type = "blur", callback = blurListener)

        onDispose {
            document.removeEventListener(type = "visibilitychange", callback = visibilityListener)
            window.removeEventListener(type = "focus", callback = focusListener)
            window.removeEventListener(type = "blur", callback = blurListener)
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun isHidden(): Boolean = js(code = "document.hidden")
