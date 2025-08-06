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

val LocalWindowState = staticCompositionLocalOf<WindowState?> { null }

fun main() = application {
    val dispatcher: Dispatcher = Dispatchers()
    val locale: Locale = DesktopLocale()
    val localConfig: LocalConfig = DesktopLocalConfig()
    val remoteConfig: RemoteConfig = DesktopRemoteConfig()
    val firestore: Firestore = DesktopFirestore()
    val databaseDriver: SqlDriver = JdbcSqliteDriver(
        url = "jdbc:sqlite:${Database.NAME}",
        properties = Properties(),
        schema = AppDatabase.Schema,
    )
    val httpClient: HttpClient = HttpClientFactory.getExoplanetHttpClient()
    val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        localConfig = localConfig,
        firestore = firestore,
        databaseDriver = databaseDriver,
        httpClient = httpClient
    )
    val core: Core = AppCore(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases
    )

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
