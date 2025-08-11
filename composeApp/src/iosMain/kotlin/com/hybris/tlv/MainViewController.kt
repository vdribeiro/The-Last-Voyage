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

private val dispatcher: Dispatcher by lazy {
    Dispatchers()
}
private val locale: Locale by lazy {
    IosLocale()
}
private val localConfig: LocalConfig by lazy {
    IosLocalConfig()
}
private val remoteConfig: RemoteConfig by lazy {
    IosRemoteConfig()
}
private val firestore: Firestore by lazy {
    IosFirestore()
}
private val databaseDriver: SqlDriver by lazy {
    NativeSqliteDriver(
        schema = AppDatabase.Schema,
        name = Database.NAME
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

fun MainViewController() = ComposeUIViewController {
    App(core = core)
}
