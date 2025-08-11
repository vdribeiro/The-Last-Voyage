package com.hybris.tlv

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.hybris.tlv.config.DesktopLocalConfig
import com.hybris.tlv.config.DesktopRemoteConfig
import com.hybris.tlv.config.LocalConfig
import com.hybris.tlv.config.RemoteConfig
import com.hybris.tlv.database.Database
import com.hybris.tlv.firestore.DesktopFirestore
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.http.client.HttpClientFactory
import com.hybris.tlv.locale.DesktopLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import com.hybris.tlv.usecase.translation.getTranslation
import database.AppDatabase
import io.ktor.client.HttpClient
import java.util.Properties

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
private val firestore: Firestore by lazy {
    DesktopFirestore()
}
private val databaseDriver: SqlDriver by lazy {
    JdbcSqliteDriver(
        url = "jdbc:sqlite:${Database.NAME}",
        properties = Properties(),
        schema = AppDatabase.Schema,
    )
}
private val httpClient: HttpClient by lazy {
    HttpClientFactory.getExoplanetHttpClient()
}
private val useCases: UseCases by lazy {
    Gateways(
        dispatcher = dispatcher,
        firestore = firestore,
        databaseDriver = databaseDriver,
        httpClient = httpClient
    )
}
private val core: Core by lazy {
    AppCore(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases
    )
}

val LocalWindowState = staticCompositionLocalOf<WindowState?> { null }

fun main() = application {
    val windowState = rememberWindowState()
    CompositionLocalProvider(value = LocalWindowState provides windowState) {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = getTranslation(key = "app_name")
        ) {
            App(core = core)
        }
    }
}
