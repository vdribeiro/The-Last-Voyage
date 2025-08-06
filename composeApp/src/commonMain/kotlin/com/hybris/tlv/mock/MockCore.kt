package com.hybris.tlv.mock

import app.cash.sqldelight.db.SqlDriver
import com.hybris.tlv.AppCore
import com.hybris.tlv.Core
import com.hybris.tlv.config.CommonLocalConfig
import com.hybris.tlv.config.CommonRemoteConfig
import com.hybris.tlv.config.LocalConfig
import com.hybris.tlv.config.RemoteConfig
import com.hybris.tlv.firestore.CommonFirestore
import com.hybris.tlv.firestore.Firestore
import com.hybris.tlv.flow.CommonDispatchers
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.http.client.HttpClientFactory
import com.hybris.tlv.locale.CommonLocale
import com.hybris.tlv.locale.Locale
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.usecase.Gateways
import com.hybris.tlv.usecase.UseCases
import io.ktor.client.HttpClient

internal class MockCore(driver: SqlDriver) {
    private val dispatcher: Dispatcher = CommonDispatchers()
    private val locale: Locale = CommonLocale()
    private val localConfig: LocalConfig = CommonLocalConfig()
    private val remoteConfig: RemoteConfig = CommonRemoteConfig()
    private val firestore: Firestore = CommonFirestore()
    private val httpClient: HttpClient = HttpClientFactory.getExoplanetMockHttpClient()
    private val useCases: UseCases = Gateways(
        dispatcher = dispatcher,
        firestore = firestore,
        databaseDriver = driver,
        httpClient = httpClient,
    )
    private val core: Core = AppCore(
        dispatcher = dispatcher,
        locale = locale,
        localConfig = localConfig,
        remoteConfig = remoteConfig,
        useCases = useCases,
    )
    val navigation = Navigation(core = core)
}
