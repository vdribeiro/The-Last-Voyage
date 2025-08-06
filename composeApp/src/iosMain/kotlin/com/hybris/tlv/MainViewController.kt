package com.hybris.tlv

import androidx.compose.ui.window.ComposeUIViewController
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.analytics.Analytics
import com.hybris.tlv.config.LocalConfig
import com.hybris.tlv.config.RemoteConfig
import com.hybris.tlv.database.Database
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.http.client.HttpClientFactory
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.resource.Resource
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import database.AppDatabase
import io.ktor.client.HttpClient

fun MainViewController() = ComposeUIViewController {
    val dispatcher: Dispatcher = Dispatchers()
    val locale: Locale = AndroidLocale(context = this)
    val resource: Resource = AndroidResource(context = this)
    val analytics: Analytics = AndroidAnalytics()
    val localConfig: LocalConfig = AndroidLocalConfig(context = this)
    val remoteConfig: RemoteConfig = AndroidRemoteConfig()
    val firestore: Firestore = AndroidFirestore()
    val databaseDriver: SqlDriver = AndroidSqliteDriver(
        context = this,
        schema = AppDatabase.Schema,
        name = Database.NAME
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
        analytics = analytics,
        locale = locale,
        resource = resource,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases
    )

    App(core = core)
}
