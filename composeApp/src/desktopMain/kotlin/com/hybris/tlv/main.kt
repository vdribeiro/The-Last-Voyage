package com.hybris.tlv

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
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
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = getTranslation(key = "app_name")
    ) {
        CompositionLocalProvider(value = LocalWindowState provides windowState) {
            val focusRequester = remember { FocusRequester() }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onPointerEvent(eventType = PointerEventType.Press) { with(receiver = it.buttons) { if (isBackPressed || isSecondaryPressed) dependency.navigation.back() } }
                    .onKeySequence(sequence = konamiCode) { Telemetry.feedback(message = "Konami Code!") }
                    .focusRequester(focusRequester = focusRequester)
                    .focusable()
            ) { App() }
            LaunchedEffect(key1 = Unit) { focusRequester.requestFocus() }
        }
    }
}

/**
 * Listen for a specific [sequence] of key presses.
 */
private fun Modifier.onKeySequence(
    sequence: List<Key>,
    onSequenceComplete: () -> Unit
): Modifier = composed {
    var progress by remember { mutableStateOf(value = 0) }
    onPreviewKeyEvent { keyEvent ->
        if (keyEvent.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
        val expectedKey = sequence.getOrNull(index = progress)
        when (keyEvent.key) {
            expectedKey -> progress++
            else -> progress = if (keyEvent.key == sequence.firstOrNull()) 1 else 0
        }
        if (progress >= sequence.size) {
            onSequenceComplete()
            progress = 0
        }
        true
    }
}

private val konamiCode = listOf(
    Key.DirectionUp, Key.DirectionUp, Key.DirectionDown, Key.DirectionDown,
    Key.DirectionLeft, Key.DirectionRight, Key.DirectionLeft, Key.DirectionRight,
    Key.B, Key.A, Key.Enter
)
