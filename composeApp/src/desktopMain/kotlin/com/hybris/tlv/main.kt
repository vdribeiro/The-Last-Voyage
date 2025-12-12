package com.hybris.tlv

import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.hybris.tlv.cheats.rememberKeySequenceCheats
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.theme.getTranslation
import javafx.embed.swing.JFXPanel
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_round

private const val TAG = "App"

private val initializeJfx by lazy {
    runCatching {
        JFXPanel()
        true
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to start JavaFX", throwable = it) }.getOrDefault(defaultValue = false)
}
internal val LocalWindowState = staticCompositionLocalOf<WindowState> { error(message = "No LocalWindowState provided") }

fun main() = application {
    Telemetry.init()
    Telemetry.info(tag = TAG, message = "App started\nJavaFX = $initializeJfx")

    val appNameTranslation = getTranslation(key = "app_name")
    val icon = runCatching {
        painterResource(resource = Res.drawable.ic_launcher_round)
    }.onFailure { Telemetry.error(tag = TAG, message = "Unable to get app icon", throwable = it) }.getOrNull()
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)
    val keyListener = rememberKeySequenceCheats()

    Window(
        title = appNameTranslation,
        icon = icon,
        state = windowState,
        onPreviewKeyEvent = keyListener,
        onCloseRequest = ::exitApplication,
    ) {
        CompositionLocalProvider(value = LocalWindowState provides windowState) {
            TLV.App()
        }
    }
}
