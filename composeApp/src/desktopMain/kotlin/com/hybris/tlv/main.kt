package com.hybris.tlv

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
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
import com.hybris.tlv.storage.DesktopLocalConfig
import com.hybris.tlv.storage.DesktopRemoteConfig
import com.hybris.tlv.storage.LocalConfig
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.translation.getTranslation

private val dispatcher: Dispatcher by lazy {
    Dispatchers()
}
private val locale: Locale by lazy {
    DesktopLocale()
}
private val localConfig: LocalConfig by lazy {
    DesktopLocalConfig()
}
private val remoteConfig: RemoteConfig by lazy {
    DesktopRemoteConfig()
}
private val sqlDriver: SqlDriver by lazy {
    createSqlDriver()
}
private val useCases: UseCases by lazy {
    Gateways(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        sqlDriver = sqlDriver,
    )
}

private val navigation: NavigationManager by lazy {
    Navigation(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases
    )
}

val LocalWindowState = staticCompositionLocalOf<WindowState?> { null }

fun main() = application {
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)

    CompositionLocalProvider(value = LocalWindowState provides windowState) {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = getTranslation(key = "app_name")
        ) {
            App(navigation = navigation)
        }
    }
}
