package com.hybris.tlv

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isBackPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.hybris.tlv.logger.Logger
import com.hybris.tlv.usecase.translation.getTranslation
import javafx.application.Platform

private val initializeJfx by lazy {
    runCatching {
        Platform.startup {}
        true
    }.getOrDefault(defaultValue = false)
}

val LocalWindowState = staticCompositionLocalOf<WindowState> { error("No LocalWindowState provided") }

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Logger.info(tag = "APP", message = "JFX = $initializeJfx")
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = getTranslation(key = "app_name")
    ) {
        CompositionLocalProvider(value = LocalWindowState provides windowState) {
            Box(modifier = Modifier.onPointerEvent(eventType = PointerEventType.Press) { pointerEvent ->
                if (pointerEvent.buttons.isSecondaryPressed || pointerEvent.buttons.isBackPressed) core.navigation.back()
            }) { App() }
        }
    }
}
