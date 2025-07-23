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
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import database.AppDatabase
import io.ktor.client.HttpClient

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dispatcher: Dispatcher = Dispatchers()
        val locale: Locale = AndroidLocale(context = this)
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

        enableEdgeToEdge()
        setContent { App(core = core) }
    }
}
