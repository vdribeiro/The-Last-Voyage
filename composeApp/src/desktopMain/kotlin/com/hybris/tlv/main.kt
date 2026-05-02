@file:ExcludeFromTesting

package com.hybris.tlv

import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.Application.dependency
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.App
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
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to start JavaFX", throwable = it)
    }.getOrDefault(defaultValue = false)
}
internal val LocalWindowState = staticCompositionLocalOf { WindowState() }

fun main() = application {
    Telemetry.info(tag = TAG, message = "JavaFX = $initializeJfx")

    val appNameTranslation = getTranslation(key = "app_name")
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)
    val icon = painterResource(resource = Res.drawable.ic_launcher_round)
    val navController = rememberNavController()
    val keyListener = rememberKeySequenceCheats(navController = navController)
    val dependency by dependency.collectAsState()

    Window(
        title = appNameTranslation,
        state = windowState,
        icon = icon,
        onPreviewKeyEvent = keyListener,
        onCloseRequest = ::exitApplication,
    ) {
        App(
            compositionValues = listOf(LocalWindowState provides windowState),
            navController = navController,
            dependency = dependency
        )
    }
}
