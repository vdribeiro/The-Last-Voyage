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
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.locale.DesktopLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.storage.Storage
import com.hybris.tlv.storage.StorageManager
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.translation.getTranslation
import java.util.concurrent.atomic.AtomicBoolean
import javafx.application.Platform

private val dispatcher: Dispatcher by lazy {
    Dispatchers()
}
private val locale: Locale by lazy {
    DesktopLocale()
}
private val storage: StorageManager by lazy {
    Storage()
}
private val sqlDriver: SqlDriver by lazy {
    createSqlDriver()
}
private val useCases: UseCases by lazy {
    Gateways(
        dispatcher = dispatcher,
        locale = locale,
        storage = storage,
        sqlDriver = sqlDriver,
    )
}

private val navigation: NavigationManager by lazy {
    Navigation(
        dispatcher = dispatcher,
        locale = locale,
        storage = storage,
        useCases = useCases
    )
}

private val isJfxInitialized = AtomicBoolean(false)

val LocalWindowState = staticCompositionLocalOf<WindowState?> { null }

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)
    if (!isJfxInitialized.getAndSet(true)) Platform.startup {}

    CompositionLocalProvider(value = LocalWindowState provides windowState) {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = getTranslation(key = "app_name")
        ) {
            Box(
                modifier = Modifier
                    .onPointerEvent(eventType = PointerEventType.Press) { pointerEvent ->
                        if (pointerEvent.buttons.isSecondaryPressed || pointerEvent.buttons.isBackPressed) {
                            navigation.back()
                        }
                    }
            ) {
                App(navigation = navigation)
            }
        }
    }
}
