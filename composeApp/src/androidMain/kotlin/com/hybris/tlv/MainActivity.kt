package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.hybris.tlv.config.AndroidLocalConfig
import com.hybris.tlv.config.AndroidRemoteConfig
import com.hybris.tlv.config.LocalConfig
import com.hybris.tlv.config.RemoteConfig
import com.hybris.tlv.database.Database
import com.hybris.tlv.firestore.AndroidFirestore
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.http.client.HttpClientFactory
import com.hybris.tlv.locale.AndroidLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import database.AppDatabase
import io.ktor.client.HttpClient

private val dispatcher: Dispatcher by lazy {
    Dispatchers()
}
private val locale: Locale by lazy {
    AndroidLocale(context = applicationContext)
}
private val localConfig: LocalConfig by lazy {
    AndroidLocalConfig(context = applicationContext)
}
private val remoteConfig: RemoteConfig by lazy {
    AndroidRemoteConfig()
}
private val firestore: Firestore by lazy {
    AndroidFirestore()
}
private val databaseDriver: SqlDriver by lazy {
    AndroidSqliteDriver(
        context = applicationContext,
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
private val navigation: Navigation by lazy {
    Navigation(core = core)
}

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { App(navigation = navigation) }
    }
}
