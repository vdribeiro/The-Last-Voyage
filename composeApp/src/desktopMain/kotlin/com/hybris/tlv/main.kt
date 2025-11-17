package com.hybris.tlv

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
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
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to start JavaFX", throwable = it) }.getOrDefault(defaultValue = false)
}

internal val LocalWindowState = staticCompositionLocalOf<WindowState> { error("No LocalWindowState provided") }

private val dependency: Dependency by lazy { Dependency() }

fun main() = application {
    Telemetry.init()
    Telemetry.info(tag = TAG, message = "JavaFX = $initializeJfx")

    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val appNameTranslation = remember(key1 = translationVersion) { getTranslation(key = "app_name") }

    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)
    Window(
        title = appNameTranslation,
        state = windowState,
        onCloseRequest = ::exitApplication,
        onPreviewKeyEvent = rememberCheats(config = dependency.config)
    ) {
        CompositionLocalProvider(value = LocalWindowState provides windowState) { App(dependency = dependency) }
    }
}
