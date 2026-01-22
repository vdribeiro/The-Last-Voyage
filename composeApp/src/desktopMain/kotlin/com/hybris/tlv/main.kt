@file:ExcludeFromTesting

package com.hybris.tlv

import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.hybris.tlv.TLV.App
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.cheats.rememberKeySequenceCheats
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
internal val LocalWindowState = staticCompositionLocalOf { WindowState() }

fun main() = application {
    Telemetry.init()
    Telemetry.info(tag = TAG, message = "App started")
    Telemetry.info(tag = TAG, message = "JavaFX = $initializeJfx")

    val appNameTranslation = getTranslation(key = "app_name")
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)
    val icon = painterResource(resource = Res.drawable.ic_launcher_round)
    val keyListener = rememberKeySequenceCheats()

    Window(
        title = appNameTranslation,
        state = windowState,
        icon = icon,
        onPreviewKeyEvent = keyListener,
        onCloseRequest = ::exitApplication,
    ) {
        App(modifier = Modifier, LocalWindowState provides windowState)
    }
}
