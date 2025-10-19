package com.hybris.tlv

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.hybris.tlv.flow.launch
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.component.modifier.onSequence
import com.hybris.tlv.ui.theme.component.modifier.registerBackNavigation
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation
import javafx.embed.swing.JFXPanel

private const val TAG = "APP"

private val initializeJfx by lazy {
    runCatching {
        JFXPanel()
        true
    }.getOrElse {
        Telemetry.error(tag = TAG, message = "Unable to start JavaFX", throwable = it)
        false
    }
}

internal val LocalWindowState = staticCompositionLocalOf<WindowState> { error("No LocalWindowState provided") }

fun main() = application {
    Telemetry.init()
    Telemetry.info(tag = TAG, message = "JavaFX = $initializeJfx")

    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val appNameTranslation = remember(key1 = translationVersion) { getTranslation(key = "app_name") }

    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)
    var konamiCodeProgress by remember { mutableStateOf(value = 0) }
    Window(
        title = appNameTranslation,
        state = windowState,
        onCloseRequest = ::exitApplication,
        onPreviewKeyEvent = { keyEvent ->
            konamiCodeProgress = keyEvent.onSequence(sequence = konamiCode, progress = konamiCodeProgress) {
                Telemetry.feedback(message = "Konami Code!")
                dependency.dispatcher.default.launch {
                    dependency.config.setPreferences { it.copy(cheats = true) }
                }
            }
            false
        }
    ) {
        CompositionLocalProvider(value = LocalWindowState provides windowState) {
            App(modifier = Modifier.registerBackNavigation { dependency.navigation.back() })
        }
    }
}

private val konamiCode = listOf(
    Key.DirectionUp, Key.DirectionUp, Key.DirectionDown, Key.DirectionDown,
    Key.DirectionLeft, Key.DirectionRight, Key.DirectionLeft, Key.DirectionRight,
    Key.B, Key.A, Key.Enter
)
