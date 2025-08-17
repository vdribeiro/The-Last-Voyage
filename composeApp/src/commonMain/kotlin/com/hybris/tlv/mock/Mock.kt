package com.hybris.tlv.mock

import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.firestore.CommonFirestore
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.CommonDispatchers
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.client.CommonHttpClientFactory
import com.hybris.tlv.http.client.HttpClientFactory
import com.hybris.tlv.locale.CommonLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.storage.CommonLocalConfig
import com.hybris.tlv.storage.CommonRemoteConfig
import com.hybris.tlv.storage.LocalConfig
import com.hybris.tlv.storage.RemoteConfig
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class Mock(
    val dispatcher: Dispatcher = CommonDispatchers(),
    val locale: Locale = CommonLocale(),
    val localConfig: LocalConfig = CommonLocalConfig(),
    val remoteConfig: RemoteConfig = CommonRemoteConfig(),
    val firestore: Firestore = CommonFirestore(),
    val sqlDriver: SqlDriver = createSqlDriver(inMemory = true),
    val httpClientFactory: HttpClientFactory = CommonHttpClientFactory(),
) {
    val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        firestore = firestore,
        sqlDriver = sqlDriver,
        httpClientFactory = httpClientFactory
    )
    val navigation: NavigationManager = Navigation(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases
    )

    init {
        runBlocking {
            useCases.sync.prepopulate().last()
        }
    }
}
