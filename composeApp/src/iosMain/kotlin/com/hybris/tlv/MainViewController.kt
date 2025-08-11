package com.hybris.tlv

import androidx.compose.ui.window.ComposeUIViewController
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.config.IosLocalConfig
import com.hybris.tlv.config.IosRemoteConfig
import com.hybris.tlv.config.LocalConfig
import com.hybris.tlv.config.RemoteConfig
import com.hybris.tlv.database.Database
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.firestore.IosFirestore
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.http.client.HttpClientFactory
import com.hybris.tlv.locale.IosLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import database.AppDatabase
import io.ktor.client.HttpClient

fun MainViewController() = ComposeUIViewController {
    val dispatcher: Dispatcher = Dispatchers()
    val locale: Locale = IosLocale()
    val localConfig: LocalConfig = IosLocalConfig()
    val remoteConfig: RemoteConfig = IosRemoteConfig()
    val firestore: Firestore = IosFirestore()
    val databaseDriver: SqlDriver = NativeSqliteDriver(
        schema = AppDatabase.Schema,
        name = Database.NAME
    )
    val httpClient: HttpClient = HttpClientFactory.getExoplanetHttpClient()
    val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
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

    App(core = core)
}
