package com.hybris.tlv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isBackPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.hybris.tlv.telemetry.Telemetry
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

val LocalWindowState = staticCompositionLocalOf<WindowState> { error("No LocalWindowState provided") }

@OptIn(ExperimentalComposeUiApi::class)
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
            konamiCodeProgress = onKeySequence(
                keyEvent = keyEvent,
                sequence = konamiCode,
                progress = konamiCodeProgress,
            ) { Telemetry.feedback(message = "Konami Code!") }
            false
        }
    ) {
        CompositionLocalProvider(value = LocalWindowState provides windowState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onPointerEvent(eventType = PointerEventType.Press) { with(receiver = it.buttons) { if (isBackPressed || isSecondaryPressed) dependency.navigation.back() } }
            ) { App() }
        }
    }
}

/**
 * Processes a [keyEvent] to check its [progress] against a given key [sequence]
 * and return the new progress value after processing the event.
 */
private fun onKeySequence(
    keyEvent: KeyEvent,
    sequence: List<Key>,
    progress: Int,
    onSequenceComplete: () -> Unit
): Int {
    if (keyEvent.type != KeyEventType.KeyDown) return progress
    val expectedKey = sequence.getOrNull(index = progress)
    val newProgress = when (keyEvent.key) {
        expectedKey -> progress + 1
        else -> if (keyEvent.key == sequence.firstOrNull()) 1 else 0
    }
    if (newProgress >= sequence.size) {
        onSequenceComplete()
        return 0
    }
    return newProgress
}

private val konamiCode = listOf(
    Key.DirectionUp, Key.DirectionUp, Key.DirectionDown, Key.DirectionDown,
    Key.DirectionLeft, Key.DirectionRight, Key.DirectionLeft, Key.DirectionRight,
    Key.B, Key.A, Key.Enter
)
