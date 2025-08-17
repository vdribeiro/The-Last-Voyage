package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.firestore.AndroidFirestore
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.http.client.AndroidHttpClientFactory
import com.hybris.tlv.http.client.HttpClientFactory
import com.hybris.tlv.locale.AndroidLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.storage.AndroidLocalConfig
import com.hybris.tlv.storage.AndroidRemoteConfig
import com.hybris.tlv.storage.LocalConfig
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases

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
private val sqlDriver: SqlDriver by lazy {
    createSqlDriver()
}

private val httpClientFactory: HttpClientFactory by lazy {
    AndroidHttpClientFactory()
}
private val useCases: UseCases by lazy {
    Gateways(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        firestore = firestore,
        sqlDriver = sqlDriver,
        httpClientFactory = httpClientFactory
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

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { App(navigation = navigation) }
    }
}
